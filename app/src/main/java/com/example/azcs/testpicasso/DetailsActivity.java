package com.example.azcs.testpicasso;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    TextView mTitle , mPlot , mRate , mDate ;
    ImageView mPoster ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Movie movie = getIntent().getParcelableExtra("Movie");

        mTitle = (TextView)findViewById(R.id.title);
        mPlot = (TextView)findViewById(R.id.plot);
        mRate = (TextView)findViewById(R.id.rate);
        mDate = (TextView)findViewById(R.id.date);
        mPoster = (ImageView)findViewById(R.id.poster);

        if (movie != null){
            mTitle.setText(getString(R.string.title) +movie.getTitle());
            mRate.setText(getString(R.string.rate) + String.valueOf(movie.getRate()));
            mDate.setText(getString(R.string.release_date) +movie.getDate());
            mPlot.setText(getString(R.string.plot_synopsis) + ":\n" +movie.getPlot());
            Picasso.with(getApplicationContext())
                    .load(movie.getPpath())
                    .placeholder(R.drawable.placeholder)
                    .into(mPoster);
        }
    }
}
