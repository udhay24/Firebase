package com.example.udhay.firebase.Adapters;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    ArrayList<Uri> imageUris;

    public ImageAdapter(ArrayList<Uri> imageUris){
        this.imageUris = imageUris;
    }


    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Uri imageUri = this.imageUris.get(position);

        if(convertView == null){
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setAdjustViewBounds(true);
            convertView = imageView;
        }

        Picasso.get().load(imageUri).fit().into((ImageView) convertView);
        return convertView;
    }
}
