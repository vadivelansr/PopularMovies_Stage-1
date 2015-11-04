package com.mydomain.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.beans.Reviews;

import java.util.ArrayList;

/**
 * Created by vadivelansr on 11/2/2015.
 */
public class ReviewsAdapter extends ArrayAdapter<Reviews> {
    public ReviewsAdapter(Context context, ArrayList<Reviews> reviews){
        super(context, 0, reviews);
    }
    private static class ViewHolder{
        TextView author;
        TextView content;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Reviews reviews = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_list_item, parent, false);
            viewHolder.author = (TextView)convertView.findViewById(R.id.reviews_textview_author);
            viewHolder.content = (TextView)convertView.findViewById(R.id.reviews_textview_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.author.setText(reviews.getAuthor());
        viewHolder.content.setText(reviews.getContent());

        return convertView;
    }
}
