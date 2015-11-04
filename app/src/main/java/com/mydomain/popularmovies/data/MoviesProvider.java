package com.mydomain.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by vadivelansr on 10/29/2015.
 */
@ContentProvider(authority = MoviesProvider.AUTHORITY, database = MoviesDatabase.class)
public final class MoviesProvider {
    public static final String AUTHORITY = "com.mydomain.popularmovies.data.MoviesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String MOVIES = "movies";
    }
    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = MoviesDatabase.MOVIES) public static class Movies{
        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movies",
                defaultSort = MoviesColumns._ID + "ASC"
        )
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);
        @InexactContentUri(
                name = "MOVIES_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movies",
                whereColumn = MoviesColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id){
            return buildUri(Path.MOVIES, String.valueOf(id));
        }
    }
}
