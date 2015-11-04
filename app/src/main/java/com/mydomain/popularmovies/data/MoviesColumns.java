package com.mydomain.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by vadivelansr on 10/29/2015.
 */
public interface MoviesColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT)
    public static final String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT)
    public static final String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT)
    public static final String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.TEXT)
    public static final String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.TEXT)
    public static final String BACKDROP_PATH = "backdrop_path";
    @DataType(DataType.Type.TEXT)
    public static final String MOVIE_ID = "movie_id";
}
