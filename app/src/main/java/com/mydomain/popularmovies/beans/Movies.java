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

    public Movies(){

    }
    public Movies(String originalTitle,String overview,String releaseDate, String posterPath, double voteAverage){
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }
    public Movies(Parcel in){
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.voteAverage = in.readDouble();
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(voteAverage);
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



}
