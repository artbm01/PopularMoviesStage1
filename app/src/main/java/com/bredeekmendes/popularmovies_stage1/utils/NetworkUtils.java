package com.bredeekmendes.popularmovies_stage1.utils;

import android.content.Context;
import android.net.Uri;

import com.bredeekmendes.popularmovies_stage1.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by arthur on 5/3/18.
 */

public class NetworkUtils {

    private static String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static String TOP_RATED = "top_rated";
    private static String POPULAR = "popular";
    private static final String QUERY_PARAM = "api_key";
    private static String API_KEY = "";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    /**
     * This method will generate the appropriate URL based on the user preference
     * @param context
     * @param preferredSortingMethod
     * @return
     */
    public static URL getUrl (Context context, String preferredSortingMethod){
        if (preferredSortingMethod.equals(context.getResources().getString(R.string.pref_sortby_rate))){
            return buildTopRatedUrl();
        }
        if (preferredSortingMethod.equals(context.getResources().getString(R.string.pref_sortby_popularity))){
            return buildPopularUrl();
        }
        else return null;
    }

    private static URL buildTopRatedUrl() {
        Uri movieQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TOP_RATED)
                .appendQueryParameter(QUERY_PARAM, API_KEY)
                .build();
        try{
            URL movieQueryUrl = new URL(movieQueryUri.toString());
            return movieQueryUrl;
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    private static URL buildPopularUrl() {
        Uri movieQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(POPULAR)
                .appendQueryParameter(QUERY_PARAM, API_KEY)
                .build();
        try{
            URL movieQueryUrl = new URL(movieQueryUri.toString());
            return movieQueryUrl;
        } catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is responsible to make the URL request and returns the JSON answer from the server
     * @param url
     * @return
     * @throws IOException
     */
    public static String getJsonResponseFromUrlRequest (URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * THis method is responsible for building the correct URL to retrieve the movie poster image
     * @param imageUrl
     * @return
     */
    public static String getImageUrl(String imageUrl){
        return IMAGE_BASE_URL + IMAGE_SIZE + imageUrl;
    }

}
