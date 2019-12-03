package com.rotem.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends BaseAdapter
{
    private Context context;
    private List<Movie> lstItem;

    public MovieAdapter(Context context)
    {
        lstItem = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return lstItem.size();
    }

    @Override
    public Movie getItem(int pos)
    {
        return lstItem.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return (long) pos;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        try
        {
            if (view == null)
            {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
            }

            Movie movie = getItem(position);

            String name = movie.getName();
            String description = movie.getDescription();
            String score = movie.getScore();
            String actors = movie.getActors();
            String imageUrl = movie.getImageUrl();

            // get all views
            ImageView imageView = view.findViewById(R.id.movie_image);
            TextView nameView = view.findViewById(R.id.movie_title);
            TextView descriptionView = view.findViewById(R.id.movie_shortDes);
            TextView scoreView = view.findViewById(R.id.movie_score);
            TextView actorsView = view.findViewById(R.id.movie_actors);

            // set all data in view
            if (imageUrl.isEmpty())
            {
                Picasso.with(context).load(R.drawable.place_holder).into(imageView);
            }
            else
            {
                Picasso.with(context).load(imageUrl).into(imageView);
            }
            nameView.setText(name);
            descriptionView.setText(description);
            scoreView.setText(score);
            actorsView.setText(actors);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }

    public void updateList(List<Movie> lstMovie)
    {
        this.lstItem.clear();
        this.lstItem.addAll(lstMovie);
    }

    public void remove(Movie paramMovie)
    {
        for (int index = lstItem.size() - 1; index >= 0; index--)
        {
            Movie movie = lstItem.get(index);
            if (paramMovie.getId() == movie.getId())
            {
                lstItem.remove(index);
                return;
            }
        }
    }

    public void add(Movie movie)
    {
        lstItem.add(movie);
    }
}

