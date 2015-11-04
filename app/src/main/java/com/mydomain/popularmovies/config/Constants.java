package com.mydomain.popularmovies.config;

/**
 * Created by vadivelansr on 10/19/2015.
 */
public interface Constants {
    String API_KEY = "";
    String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    String BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
    String MOVIE_TRAILER_URL = "/videos" + "?api_key=" + API_KEY;
    String MOVIE_REVIEWS_URL = "/reviews" + "?api_key=" + API_KEY;
    String POPULARITY_DESC = "popularity.desc";
    String HIGHEST_RATED_DESC = "vote_average.desc";
    String POPULAR_MOVIES_URL = BASE_URL + POPULARITY_DESC + "&api_key=" + API_KEY +"&page=";
    String HIGHEST_RATED_MOVIES_URL = BASE_URL + HIGHEST_RATED_DESC + "&api_key=" + API_KEY +"&page=";
    String MOVIE_DETAILS = "movie_details";
    String RESULTS = "results";
    String ID = "id";
    String ORIGINAL_TITLE = "original_title";
    String POSTER_PATH = "poster_path";
    String OVERVIEW = "overview";
    String VOTE_AVERAGE = "vote_average";
    String RELEASE_DATE = "release_date";
    String BACKDROP_PATH = "backdrop_path";
    String MOVIE_ID = "id";
    String NAME = "name";
    String KEY = "key";
    String AUTHOR = "author";
    String CONTENT = "content";
    String IMAGE_RESOLUTION = "w185";
    String BACKDROP_RESOLUTION = "w300";
    String RATING = "/10";
    String KEY_PARCELABLE_RESULTS_LIST = "results_list";
    String KEY_LIST_POSITION = "list_position";
    String KEY_ITEM_POSITION = "item_position";
    String SEPERATOR = " : ";
    int PAGES_THRESHOLD = 1000;
}
