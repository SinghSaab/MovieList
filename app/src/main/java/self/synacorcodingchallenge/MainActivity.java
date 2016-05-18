package self.synacorcodingchallenge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    API_KEY for movie database
    private static final String api_key = "242bb6fd5a1f11b52d9b2de77cbe119f";

    //    Variables initialize
    private ProgressDialog pDialog;
    private List<MovieParam> movieList = new ArrayList<MovieParam>();
    private ListView listView;
    private LVadapter adapter;
    Snackbar snackbar;
    FloatingActionButton fab;

    //    JSONObject for requesting queries using Volley
    //    Volley would cache network calls and hence saves bandwidth
    JsonObjectRequest movieReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.make(view, "Check ActionBar menu for sorting options", snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

//        List View components
        listView = (ListView) findViewById(R.id.list);
        adapter = new LVadapter(this, movieList);
        listView.setAdapter(adapter);

//        Progress Dialog box components
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

//        Crafting request for the Volley JSONObjectRequest and JSONArrayRequest
//        Every page consists of 20 results and hence 5 pages for 100 results
        for (int pageNo = 1; pageNo <= 5; pageNo++) {
            movieReq = new JsonObjectRequest(Request.Method.GET, craftURL(pageNo), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressDialog();

//                            Parse JSONObject for  array and respective object fields
                            try {
                                JSONArray arr = response.getJSONArray("results");
                                for (int objectCount = 0; objectCount < arr.length(); ++objectCount) {
                                    JSONObject obj = arr.getJSONObject(objectCount);
                                    MovieParam movie = new MovieParam();
                                    movie.setYear(obj.getString("release_date"));
                                    movie.setTitle(obj.getString("title"));

//                                  OR make changes as depicted below for YEAR only rather than YYYY-MM-DD
//                                  movie.setYear(obj.getString("release_date").split("-")[0]);

                                    movieList.add(movie);
                                }

//                                Notify adapter for any data changes.
//                                Missing this will not update the view when elements were chosen to be sorted
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "JSON response Error: " + error, Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            });

//            Adding the crafted Volley request to the RequestedQueue
            AppController.getInstance().addToRequestQueue(movieReq);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        If Title is to be sorted
        if (id == R.id.action_sortTitle) {
            Collections.sort(movieList, new NewComparator(id));
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Sorted by titles alphabetically", Toast.LENGTH_SHORT).show();
            return true;
        }

//        If Year is to be sorted
        if (id == R.id.action_sortYear) {
            Collections.sort(movieList, new NewComparator(id));
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Sorted by descending year", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }


    private void hideProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private String craftURL(int pageNo) {
        return "http://api.themoviedb.org/3/discover/movie?" +
                "page=" + String.valueOf(pageNo) +
                "&vote_count.gte=250&sort_by=vote_count.desc" +
                "&api_key=" + api_key;
    }
}
