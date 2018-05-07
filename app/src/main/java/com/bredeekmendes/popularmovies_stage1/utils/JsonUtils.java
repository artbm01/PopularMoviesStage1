/**
 * Created by arthur on 5/3/18.
 */
package com.bredeekmendes.popularmovies_stage1.utils;

import android.util.Log;

import com.bredeekmendes.popularmovies_stage1.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TITLE = "title";
    private static final String POSTER = "poster_path";
    private static final String SYNOPSIS = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE = "release_date";
    private static final String RESULTS = "results";

    /**
     * This method is responsible for parsing the JSON data and encapsulating it in the Movie object.
     * @param jsonString
     * @return
     */
    public static List<Movie> parseMovieJson (String jsonString){

        List<Movie> movieList = new ArrayList<>();
        if (!jsonString.isEmpty()){
            try{
                JSONObject movies = new JSONObject(jsonString);
                JSONArray results = movies.getJSONArray(RESULTS);
                for (int i=0; i<results.length(); i++){
                    JSONObject item = results.getJSONObject(i);
                    String title = item.getString(TITLE);
                    String synopsis = item.getString(SYNOPSIS);
                    String rating = item.getString(RATING);
                    String releaseDate = item.getString(RELEASE);
                    String poster_image = item.getString(POSTER);
                    Movie movie = new Movie();
                    movie.setTitle(title);
                    movie.setSynopsis(synopsis);
                    movie.setRating(rating);
                    movie.setReleaseDate(releaseDate);
                    movie.setImage(poster_image);
                    movieList.add(movie);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return movieList;
    }
}
