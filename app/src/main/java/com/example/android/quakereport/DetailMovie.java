package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.Arrays;

import static android.provider.MediaStore.Video.Thumbnails.VIDEO_ID;

public class DetailMovie extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener{

    public static final String DEVELOPER_KEY = "AIzaSyBWm-UDvWI0uImUQkFmpKJbyBzau1MrkCs";
    private static String VIDEO_ID = null;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerFragment myYouTubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);

        //getting reference to all the textViews
        TextView titleTextView = (TextView) findViewById(R.id.mdName);
        TextView ratingTextView = (TextView) findViewById(R.id.mdRating);
        TextView yearTextView = (TextView) findViewById(R.id.mdYear);
        TextView summaryTextView = (TextView) findViewById(R.id.mdSummary);
        TextView genreTextView = (TextView) findViewById(R.id.mdGenre);
        TextView runtimeTextView = (TextView) findViewById(R.id.mdRuntime);
        ImageView imageView = (ImageView) findViewById(R.id.mdImage);
        Button downloadButton = (Button) findViewById(R.id.mdDownload);
        Button imdbButton = (Button) findViewById(R.id.mdImdb);

        //getting vales via intent to display in detailed format
        String title = getIntent().getStringExtra("Movie Title");
        double rating = getIntent().getExtras().getDouble("Movie Rating");
        int year = getIntent().getExtras().getInt("Movie Year");
        String finalresult = new Double(rating).toString();
        String summary = getIntent().getStringExtra("Movie Summary");
        final String youtube = getIntent().getStringExtra("Movie Youtube");
        String [] genre = getIntent().getStringArrayExtra("Movie Genre");
        int runtime = getIntent().getExtras().getInt("Movie Runtime");
        String image = getIntent().getStringExtra("Movie Image");
        final String download = getIntent().getStringExtra("Movie Download");
        String size = getIntent().getStringExtra("Movie Size");
        final String imdb = getIntent().getStringExtra("Movie Imdb");



        //assniging values to all the fields
        titleTextView.setText(title);
        ratingTextView.setText(finalresult);
        yearTextView.setText( Integer.toString(year));
        summaryTextView.setText(summary);
        genreTextView.setText(Arrays.toString(genre).replaceAll("\\[|\\]", ""));
        runtimeTextView.setText(Integer.toString(runtime));
        downloadButton.setText(size);

        //loads the image into the imageview ie the movie poster.
        Picasso.with(this)
                .load(image)
                .into(imageView);
        imageView.setImageURI(Uri.parse(image));

        //clicking this button opens up the imdb page for more data usin imdb core in json.
        imdbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.imdb.com/title/" + imdb;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //clicking download buttons opens up the download torrent page and downloads the movie (from first url in jsonArray "torrents")
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(download));
                startActivity(i);
            }
        });

        VIDEO_ID = youtube;



        //initializing youtube player
        myYouTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager()
                .findFragmentById(R.id.youtubeplayerfragment);
        myYouTubePlayerFragment.initialize(DEVELOPER_KEY, this);

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
// Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView)findViewById(R.id.youtubeplayerfragment);
    }
}




