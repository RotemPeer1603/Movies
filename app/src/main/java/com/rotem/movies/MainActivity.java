package com.rotem.movies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
{
    public ListView listViewMovie;
    public MovieAdapter adapter;
    public static final String restUrl = "https://movies-rotem.herokuapp.com/movie";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAdd = findViewById(R.id.fab);


        adapter = new MovieAdapter(this);
        listViewMovie = findViewById(R.id.main_listView);
        listViewMovie.setClickable(true);
        setClickEventListener();
        setLongEventListener();
        listViewMovie.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getBaseContext(), UpdateMovieActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadMoviesFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadMoviesFromServer()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        // making the Json Request from the server
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, restUrl, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
                updateMovieList(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Request Error", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void updateMovieList(JSONArray response)
    {
        try
        {
            List<Movie> lstMovies = new ArrayList<>();
            for (int index = 0; index < response.length(); index++)
            {
                JSONObject movie;
                movie = response.getJSONObject(index);

                int id = movie.getInt("id");
                String name = movie.getString("name");
                String score = movie.getString("score");
                String description = movie.getString("description");
                String actors = movie.getString("actors");
                String imageUrl = movie.getString("imageUrl");

                Movie newMovie = new Movie(id, name, description, score, actors, imageUrl);
                lstMovies.add(newMovie);
            }
            adapter.updateList(lstMovies);
            adapter.notifyDataSetChanged();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void setClickEventListener()
    {
        listViewMovie.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Movie movie = adapter.getItem(position);
                Intent intent = new Intent(getBaseContext(), UpdateMovieActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
    }

    public void setLongEventListener()
    {
        listViewMovie.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("The movie will be deleted from the list!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                Movie movie = (Movie) listViewMovie.getItemAtPosition(position);
                                deleteRequest(movie);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    public void deleteRequest(Movie movie)
    {

        JSONObject js = new JSONObject();
        try
        {
            js.put("id", String.valueOf(movie.getId()));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest putRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                restUrl + "/" + movie.getId(),
                js,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                        Log.d("put: ", "loaded: " + response);
                        loadMoviesFromServer();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset-utf-8");
                return params;
            }
        };
        queue.add(putRequest);
    }
}
