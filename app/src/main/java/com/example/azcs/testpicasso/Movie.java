package com.example.azcs.testpicasso;


import android.os.Parcel;
import android.os.Parcelable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by azcs on 30/07/16.
 */
public class Movie implements Parcelable {
    String title;
    String ppath;
    String plot;
    float  rate ;
    String date ;

    public Movie(String title, String ppath, String plot, float rate, String data) {
        this.title = title;
        this.ppath = ppath;
        this.plot = plot;
        this.rate = rate;
        this.date = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPpath() {
        return ppath;
    }

    public void setPpath(String ppath) {
        this.ppath = ppath;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float RATE) {
        this.rate = RATE;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*
    I think to set Date as Date type to make it easy if i decide to add a calender or something related to date calculation
     */
    private Date parseDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
        Date movie = null ;
        try {
            movie = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return movie ;
    }

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(ppath);
        dest.writeString(plot);
        dest.writeFloat(rate);
        dest.writeString(date);
    }

    public Movie (Parcel in){
        this.title = in.readString();
        this.ppath = in.readString();
        this.plot = in.readString();
        this.rate = in.readFloat();
        this.date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
