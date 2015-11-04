package com.mydomain.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mydomain.popularmovies.R;
import com.mydomain.popularmovies.beans.Trailers;

import java.util.ArrayList;

/**
 * Created by vadivelansr on 11/2/2015.
 */
public class TrailersAdapter extends ArrayAdapter<Trailers> {
    public TrailersAdapter(Context context, ArrayList<Trailers> trailers){
        super(context, 0, trailers);
    }
    private static class ViewHolder{
        TextView textView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Trailers trailers = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailers_list_item, parent, false);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.trailers_textview_trailername);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText(trailers.getName());
        return convertView;
    }
}
