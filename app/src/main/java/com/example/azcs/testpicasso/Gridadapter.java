package com.example.azcs.testpicasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by azcs on 29/07/16.
 */
public class Gridadapter extends BaseAdapter {
    Context mContext ;
    List<String> mImage  ;

    public Gridadapter(Context c , List<String> img) {
        mContext = c ;
        mImage = img ;
    }

    @Override
    public int getCount() {
        return mImage.size();
    }

    @Override
    public Object getItem(int position) {
        return mImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView ;
        if(convertView == null){
            imageView = new ImageView(mContext);
        }else {
            imageView = (ImageView) convertView ;
        }
        Picasso.with(mContext)
                .load(mImage.get(position))
                .placeholder(R.drawable.placeholder)
                .into(imageView);
        return imageView;
    }

}
