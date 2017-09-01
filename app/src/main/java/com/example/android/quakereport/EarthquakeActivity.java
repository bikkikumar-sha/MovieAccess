package com.example.android.quakereport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.R.drawable.search;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    private Boolean shouldLoadMoreData = false;
    //clears the adapter to display the search results (which automatically sets the page number to 1)
    private Boolean isNewQuerySoClearTheAdapter = false;
    //usde to fix the bug that causes data to load multiple times when the end of the list is reached.
    private int preLast;
    private String searchQuery = "0";
    private static final String USGS_REQUEST_URL =
            "https://yts.ag/api/v2/list_movies.json?limit=15";


    /** Adapter for the list of earthquakes */
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        final ListView movieListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        movieListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Movie currentMovie = mAdapter.getItem(position);
//                // Convert the String URL into a URI object (to pass into the Intent constructor)
//                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
//
//                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
//
//                startActivity(websiteIntent);
                // Send the intent to launch a new activity
                String title = currentMovie.getmTitle();
                Double rating = currentMovie.getmRating();
                int year = currentMovie.getmYear();
                String summary = currentMovie.getmSummary();
                String genre[] = currentMovie.getmGenre();
                String youtube = currentMovie.getmYoutube();
                int runtime = currentMovie.getmRuntime();
                String image = currentMovie.getmImage();
                String imdb = currentMovie.getmImdb();
                String download = currentMovie.getmDownload();
                String size = currentMovie.getmSize();


                //sending data through intent to movieDetail activity to display detailed view

                Intent intent = new Intent(getApplicationContext(), DetailMovie.class);
                Log.d("title is ", title);
                intent.putExtra("Movie Title", title);
                intent.putExtra("Movie Rating", rating);
                intent.putExtra("Movie Year", year);
                intent.putExtra("Movie Summary", summary);
                intent.putExtra("Movie Genre", genre);
                intent.putExtra("Movie Youtube", youtube);
                intent.putExtra("Movie Runtime", runtime);
                intent.putExtra("Movie Image", image);
                intent.putExtra("Movie Imdb", imdb);
                intent.putExtra("Movie Download", download);
                intent.putExtra("Movie Size", size);

                startActivity(intent);


            }
        });

        //sets and on scroll lister to see if user reached end of the list

        movieListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                switch(view.getId())
                {
                    case R.id.list:

                        // Make your calculation stuff here. You have all your
                        // needed info from the parameters of this function.

                        // Sample calculation to determine if the last
                        // item is fully visible.
                        final int lastItem = firstVisibleItem + visibleItemCount;
                        if(lastItem == totalItemCount)
                        {
              //              if(shouldLoadMoreData && (totalItemCount == lastPage*15))

                            if(preLast!=lastItem)
                            {
                                shouldLoadMoreData = false;
                                int page;
                                int limit = 15;
                                //to avoid multiple calls for last item
                                Log.d("Last", "Last");
                                Log.d("firstVisibleItem", String.valueOf(firstVisibleItem));
                                Log.d("visibleItemCount", String.valueOf(visibleItemCount));
                                Log.d("totalItemCount", String.valueOf(totalItemCount));
                                Log.d("sld", String.valueOf(shouldLoadMoreData));

                                page = (totalItemCount/limit)+1;
                                if(totalItemCount%limit == 0)       //checks if the page has elements that are multiple of limit, if not, then probably
                                {                                   //the data queue is empty so don't load.
                                    Log.d("i who reqested data", "scroller");//todo (proper fix)to extract number of data from the json and strop requestMovieData() once the whole page is loaded.
                                    requestMovieData(page, searchQuery);
                                    Log.d("Loading page: ", String.valueOf(page));
                                    preLast = lastItem;
                                }
                            }
                        }
                }

            }
        });

        final EditText searchEditText = (EditText) findViewById(R.id.mdSearchText);
        //search needs to be done so this module sends the searchQuery to the requestMovieData()
        Button searchButton = (Button) findViewById(R.id.mdSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEditText != null) {
                    searchQuery = searchEditText.getText().toString();
                    searchQuery = searchQuery.replace(" ","_");
                    isNewQuerySoClearTheAdapter = true;                     //new query set to true since serch is being done
                    Log.d("i who reqested data", "the search query");
                    requestMovieData(1, searchQuery);
                }
            }
        });
        // Start the AsyncTask to fetch the earthquake data for the first time
        //    MovieAsyncTask task = new MovieAsyncTask();
         //   task.execute(USGS_REQUEST_URL);
        //serchQuery is send as "0" because no search has been performed yet (it displays the data for the first time(ie. oncreate))
        Log.d("i who reqested data", "on create method");
        requestMovieData(1, searchQuery);
    }


    //used to build the url based on user action
    //when the query_term is "0", the api returns all movie list which is equivalent to not doing a query at all..

    private void requestMovieData(int page, String searchQuery) {

        Log.d("requesting page ", String.valueOf(page));
            //if there really is a search then do..

        String baseUrl = "https://yts.ag/api/v2/list_movies.json?limit=15&page=" + page + "&query_term=" + searchQuery;
        Log.d("Querying search", baseUrl);
        MovieAsyncTask task = new MovieAsyncTask();
        task.execute(baseUrl);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class MovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {
        ProgressDialog progressDialog = new ProgressDialog(EarthquakeActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
            super.onPreExecute();
        }

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link Movie}s as the result.
         */



        @Override
        protected List<Movie> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Movie> result = QueryUtils.fetchMovieData(urls[0]);
            //fixes "mutiple page load on scroll listener" bug...
            //since the new data has already have been appended, so @Param shouldLoadMoreData is turned off
            Log.d("sld", String.valueOf(shouldLoadMoreData));
            shouldLoadMoreData = true;
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Movie> data) {
            // Clear the adapter of previous Movies data as a completely new seach data (with a new search query) has been requested

            if(isNewQuerySoClearTheAdapter)
            {
                mAdapter.clear();
                isNewQuerySoClearTheAdapter = false;
            }

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
                Log.d("data added","15 elements");
            }

            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }


}