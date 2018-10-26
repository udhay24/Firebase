package com.example.udhay.firebase.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udhay.firebase.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Uri> documentUris;

    public void DocumentAdapter(Context context , ArrayList<Uri> uris){
        this.context = context;
        documentUris = uris;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return documentUris.size();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder{

        @Nullable
        @BindView(R.id.document_icon) ImageView iconImageView;
        @Nullable @BindView(R.id.document_name) TextView nameTextView;

        public DocumentViewHolder(View view){
            super(view);
            ButterKnife.bind(view.getContext() , view);

        }

        public ImageView geticonImageView(){
            return iconImageView;
        }

        public TextView getNameTextView(){
            return nameTextView;
        }

    }
}
