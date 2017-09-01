/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

/**
 * An {@link Movie} object contains information related to a single earthquake.
 */
public class Movie {

    /** Magnitude of the earthquake */
    private double mRating;
    private String mTitle;
    private int mYear;
    private int mId;
    private String mSummary;
    private String [] mGenre;
    private String mYoutube;
    private String mImage;
    private int mRuntime;
    private String mImdb;
    private String mDownload;
    private String mSize;

    public Movie(double mRating, String mTitle, int mYear, int mId, String mSummary, String[] mGenre, String mYoutube, String mImage, int mRuntime, String mImdb, String mDownload, String mSize) {
        this.mRating = mRating;
        this.mTitle = mTitle;
        this.mYear = mYear;
        this.mId = mId;
        this.mSummary = mSummary;
        this.mGenre = mGenre;
        this.mYoutube = mYoutube;
        this.mImage = mImage;
        this.mRuntime = mRuntime;
        this.mImdb = mImdb;
        this.mDownload = mDownload;
        this.mSize = mSize;
    }

    public String getmSummary() {
        return mSummary;
    }

    public String getmImage() {
        return mImage;
    }

    public int getmRuntime() {
        return mRuntime;
    }

    public String getmImdb() {
        return mImdb;
    }

    public String getmDownload() {
        return mDownload;
    }

    public String getmSize() {
        return mSize;
    }

    public double getmRating() {

        return mRating;

    }

    public String getmTitle() {
        return mTitle;
    }

    public int getmYear() {
        return mYear;
    }

    public String[] getmGenre() {
        return mGenre;
    }

    public String getmYoutube() {
        return mYoutube;
    }

    public int getmId() {
        return mId;
    }
/**
     * Constructs a new {@link Movie} object.
     *
     * @param magnitude is the magnitude (size) of the earthquake
     * @param location is the location where the earthquake happened
     * @param timeInMilliseconds is the time in milliseconds (from the Epoch) when the
     *                           earthquake happened
     * @param url is the website URL to find more details about the earthquake
     */

}