package self.synacorcodingchallenge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String api_key = "242bb6fd5a1f11b52d9b2de77cbe119f";

    // Movies json url
    private ProgressDialog pDialog;
    private List<MovieParam> movieList = new ArrayList<MovieParam>();
    private ListView listView;
    private LVadapter adapter;

    JsonObjectRequest movieReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView) findViewById(R.id.list);
        adapter = new LVadapter(this, movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        // Creating volley request obj for JSONObject
        for (int pageNo = 1; pageNo <= 5; pageNo++) {
            movieReq = new JsonObjectRequest(Request.Method.GET, craftURL(pageNo), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideProgressDialog();
                            // Parsing json
                            try {
                                JSONArray arr = response.getJSONArray("results");
                                for (int objectCount = 0; objectCount < arr.length(); objectCount++) {
                                    JSONObject obj = arr.getJSONObject(objectCount);
                                    MovieParam movie = new MovieParam();
                                    movie.setTitle(obj.getString("title"));
                                    movie.setYear(obj.getString("release_date"));
                                    // adding object values to movies array
                                    movieList.add(movie);
                                }
                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hideProgressDialog();
                }
            });
// Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sortTitle) {
            Collections.sort(movieList, new NewComparator(id));
            adapter.notifyDataSetChanged();
            return true;
        }

        if (id == R.id.action_sortYear) {
            Collections.sort(movieList, new NewComparator(id));
            adapter.notifyDataSetChanged();
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
        final String urlDefault = "http://api.themoviedb.org/3/discover/movie?" +
                "page=" + String.valueOf(pageNo) + "&vote_count.gte=250&sort_by=vote_count.desc" +
                "&api_key=" + api_key;
        return urlDefault;
    }
}
