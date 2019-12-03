package com.rotem.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateMovieActivity extends AppCompatActivity
{

    public final static int RT_CODE_IMAGE_URL = 21;

    private ImageView image;
    private EditText txtName;
    private EditText txtScore;
    private EditText txtDesc;
    private EditText txtActors;
    private Button btnUpdate;
    private Button btnChooseImage;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        image = findViewById(R.id.image);
        txtName = findViewById(R.id.txtName);
        txtScore = findViewById(R.id.txtScore);
        txtActors = findViewById(R.id.txtActors);
        txtDesc = findViewById(R.id.txtDesc);

        Intent intent = getIntent();

        movie = (Movie) intent.getSerializableExtra("movie");
        if (movie == null)
        {
            movie = new Movie();
        }
        else
        {
            txtName.setText(movie.getName());
            txtScore.setText(movie.getScore());
            txtActors.setText(movie.getActors());
            txtDesc.setText(movie.getDescription());
            if (!movie.getImageUrl().isEmpty())
            {
                Picasso.with(this).load(movie.getImageUrl()).into(image);
            }
        }
        btnUpdate = findViewById(R.id.btnUpdate);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateServer();
            }
        });


        btnChooseImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChooseUrlFragment dialog = new ChooseUrlFragment();
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", movie.getImageUrl());
                dialog.setArguments(bundle);

                dialog.show(getFragmentManager(), "Choose url");
            }
        });


    }

    public void updateImageUrl(String imageUrl)
    {
        if (imageUrl != null && !imageUrl.isEmpty())
        {
            movie.setImageUrl(imageUrl);
            Picasso.with(this).load(imageUrl).into(image);
        }
    }

    public void updateServer()
    {

        // get the data from edit text
        String name = txtName.getText().toString();
        String actors = txtActors.getText().toString();
        String score = txtScore.getText().toString();
        String description = txtDesc.getText().toString();

        if (name.equals("") || score.equals("") || score.equals("") || actors.equals(""))
        {
            Toast.makeText(this, "you have to fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // creating new JSON Object with the new movie data
        JSONObject js = new JSONObject();
        try
        {
            js.put("id", movie.getId());
            js.put("name", name);
            js.put("score", score);
            js.put("description", description);
            js.put("actors", actors);
            js.put("imageUrl", movie.getImageUrl());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        // make a request for POST
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                MainActivity.restUrl,
                js,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Toast.makeText(UpdateMovieActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                        Log.d("put: ", "loaded: " + response);
                        finish();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });
        queue.add(postRequest);
    }
}
