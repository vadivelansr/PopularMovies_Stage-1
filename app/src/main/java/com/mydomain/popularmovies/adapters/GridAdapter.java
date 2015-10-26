package com.mydomain.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.beans.Movies;
import com.mydomain.popularmovies.config.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by techkt.com on 10/9/2015.
 */
public class GridAdapter extends ArrayAdapter<Movies> {
    private Context context;
    private ArrayList<Movies> moviesList;
    public GridAdapter(Context context, ArrayList<Movies> moviesList){
        super(context, 0 , moviesList);
        this.context = context;
        this.moviesList = moviesList;
    }
    public void setList(ArrayList<Movies> paramList){
        moviesList = paramList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        Movies results = getItem(position);
        ViewHolder holder;
        if(view == null){
            view  = LayoutInflater.from(context).inflate(R.layout.grid_item_view, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)view.findViewById(R.id.grid_image_item);
            view.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        String url = Constants.POSTER_BASE_URL + Constants.IMAGE_RESOLUTION + results.getPosterPath();
        Picasso.with(context).load(url).into(holder.imageView);
        return view;
    }

    static class ViewHolder{
        ImageView imageView;
    }
}
