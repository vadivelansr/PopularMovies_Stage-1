package com.mydomain.popularmovies.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vadivelansr on 10/19/2015.
 */
public class Movies implements Parcelable{
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private double voteAverage;
    private String backdropPath;
    private String movieID;

     public Movies(){
    }

    public Movies(Parcel in){
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readDouble();
        this.backdropPath = in.readString();
        this.movieID = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(voteAverage);
        dest.writeString(backdropPath);
        dest.writeString(movieID);
    }
    public int describeContents(){
        return 0;
    }
    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>(){
        public Movies createFromParcel(Parcel in){
            return new Movies(in);
        }
        public Movies[] newArray(int size){
            return new Movies[size];
        }
    };
    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }


}
