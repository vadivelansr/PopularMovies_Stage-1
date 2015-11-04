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

public class GridAdapter extends ArrayAdapter<Movies> {
    public GridAdapter(Context context, ArrayList<Movies> moviesList){
        super(context, 0 , moviesList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        Movies results = getItem(position);
        ViewHolder holder;
        if(view == null){
            view  = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_view, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)view.findViewById(R.id.grid_image_item);
            view.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        String url = Constants.POSTER_BASE_URL + Constants.IMAGE_RESOLUTION + results.getPosterPath();
        Picasso.with(getContext()).load(url)
                .placeholder(R.drawable.placeholder_grid)
                .into(holder.imageView);
        return view;
    }

    static class ViewHolder{
        ImageView imageView;
    }
}
