package com.example.udhay.firebase.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udhay.firebase.Interfaces.DocumentsOnClick;
import com.example.udhay.firebase.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    DocumentsOnClick documentOnClick;

    Context context;
    ArrayList<Uri> documentUris;
    ArrayList<String> nameUris;

    public DocumentAdapter(Context context , ArrayList<Uri> uris , ArrayList<String> nameUris , DocumentsOnClick documentOnClick){
        this.context = context;
        documentUris = uris;
        this.nameUris = nameUris;
        this.documentOnClick = documentOnClick;

    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.document_view_holder , viewGroup , false);

        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder documentViewHolder, final int i) {

        documentViewHolder.getNameTextView().setText(nameUris.get(i));
        documentViewHolder.getDocumentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentOnClick.onClick(documentUris.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameUris.size();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView nameTextView;
        View view;

        public DocumentViewHolder(View view) {
            super(view);

            iconImageView = view.findViewById(R.id.document_icon);
            nameTextView = view.findViewById(R.id.document_name);
            this.view = view;
        }

        public ImageView geticonImageView() {
            return iconImageView;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }


        public View getDocumentView() {
            return this.view;
        }
    }

        public void swapData(ArrayList<Uri> data, ArrayList<String> nameUris) {

            documentUris = data;
            this.nameUris = nameUris;
        }

}


