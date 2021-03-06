/**
 * Created by arthur on 5/3/18.
 */
package com.bredeekmendes.popularmovies_stage1.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This object was created to hold the data retrieved for each movie
 */
public class Movie implements Parcelable{

    private String title;
    private String synopsis;
    private String rating;
    private String releaseDate;
    private String image;

    public Movie(){}

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return checkString(title);
    }

    public String getSynopsis() {
        return checkString(synopsis);
    }

    public String getRating() {
        return checkString(rating);
    }

    public String getReleaseDate() {
        return checkString(releaseDate).substring(0,4);
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(releaseDate);
        dest.writeString(image);
    }

    /**
     * This contructor is used to pass the Movie object to the new activity
     * @param in
     */
    private Movie (Parcel in){
        this.title = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.image = in.readString();
    }

    /**
     * This method is called to ensure there will be something to show in the UI even if there's no
     * data retrieved
     * @param string
     * @return
     */
    private String checkString(String string){
        if (string.isEmpty()){ return "No data available."; }
        else return string;
    }
}
