package com.example.azcs.testpicasso;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GridView mGridView ;
    ArrayList<String> urlOfPoster = new ArrayList<>();
    Gridadapter mGridAdapter ;
    ProgressBar mProgressBar ;
    final String TopRate = "top_rated" , MostPopular = "popular";
    TextView mTextCheckNetwork ;
    List<Movie> movies ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar)findViewById(R.id.progress);
        mTextCheckNetwork = (TextView)findViewById(R.id.txtCheckNetwork);
        mGridAdapter = new Gridadapter(getApplicationContext() , urlOfPoster);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext() , DetailsActivity.class);
                i.putExtra("Movie" , movies.get(position) );
                startActivity(i);
            }
        });
        if(Connection ()) {

            new TheMoviesData().execute(TopRate);
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mTextCheckNetwork.setText(R.string.checkNetwork);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortby ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(Connection()) {
            TheMoviesData TMDI;
            switch (item.getItemId()) {
                case R.id.highestRated:
                    TMDI = new TheMoviesData();
                    TMDI.execute(TopRate);
                    return true;
                case R.id.mostPopular:
                    TMDI = new TheMoviesData();
                    TMDI.execute(MostPopular);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        showMessage(getString(R.string.checkNetwork));
        return false ;
    }

    public boolean Connection (){
        ConnectivityManager connectivity =
                (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null
                && info.isConnectedOrConnecting()
                && connectivity.getActiveNetworkInfo() != null
                && connectivity.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void showMessage(String msg){
        Toast.makeText(getApplicationContext() , msg , Toast.LENGTH_LONG).show();
    }
    class TheMoviesData extends AsyncTask<String, Void, List<Movie>> {

        protected List<Movie> RetrieveData (String json){
            final String TITLE = "title" ;
            final String P_PATH = "poster_path" ;
            final String PLOT = "overview" ;
            final String RATE = "vote_average" ;
            final String DATE = "release_date" ;
            final String Imgurl = "http://image.tmdb.org/t/p/w185/";

            try {
                JSONObject jsonObject = new JSONObject(json) ;
                JSONArray array = jsonObject.getJSONArray("results");
                movies = new ArrayList<>();

                for(int i = 0 ; i < array.length() ; i++){
                    JSONObject oneObject = array.getJSONObject(i);
                    Movie movie = new Movie(oneObject.getString(TITLE),
                            Imgurl+oneObject.getString(P_PATH),
                            oneObject.getString(PLOT),
                            (float) oneObject.getDouble(RATE),
                            oneObject.getString(DATE));
                    movies.add(movie);
                }

             return movies ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null ;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            //Build the URL
            final String BASE_THEMOVIES = "http://api.themoviedb.org/3/movie" ;
            final String LANG = "language" ;
            final String ENGLISH = "en" ;
            final String KEY = "api_key" ;
            Uri urlBuild = Uri.parse(BASE_THEMOVIES).
                    buildUpon().
                    appendPath(params[0])
                    .appendQueryParameter(LANG , ENGLISH)
                    .appendQueryParameter(KEY , BuildConfig.API_KEY)
                    .build();
            Log.v("url", "Built URI " + urlBuild.toString());

            //declare it out of try block to close it in the finally block
            HttpURLConnection connection = null ;
            BufferedReader reader = null ;
            String TheMoviesJson = null ;
            try {

                //Try to connect to the server
                URL uri = new URL(urlBuild.toString());
                Log.v("url", "Built URI " + urlBuild.toString());
                connection =  (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                if(inputStream == null)
                    return null ;

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line ;
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");
                if(buffer.length() == 0)
                    return null;

                TheMoviesJson = buffer.toString();
                Log.v("data" , TheMoviesJson);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return RetrieveData(TheMoviesJson);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies.isEmpty())
            showMessage(getString(R.string.failed));
            else {
                urlOfPoster.clear();
                int count = 0;
                while (movies.size() > count) {
                    urlOfPoster.add(movies.get(count).getPpath());
                    count++ ;
                }
                mGridAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                mTextCheckNetwork.setText("");
            }

            super.onPostExecute(movies);
        }
    }


}
