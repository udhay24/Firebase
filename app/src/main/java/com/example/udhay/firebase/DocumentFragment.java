package com.example.udhay.firebase;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.udhay.firebase.Adapters.DocumentAdapter;
import com.example.udhay.firebase.Interfaces.DocumentsOnClick;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class DocumentFragment extends Fragment {

    private static final int SELECT_DOCUMENT_REQUEST_CODE = 120;

    @Nullable @BindView(R.id.document_recycler_view)
    RecyclerView recyclerView;

    @Nullable @BindView(R.id.fab_add_document)
    FloatingActionButton floatingActionButton;


    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    private DocumentAdapter documentAdapter;

    private DocumentsOnClick documentsOnClick;


    public DocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = firebaseDatabase.getReference(firebaseUser.getUid() + "/documents");

        documentsOnClick = new DocumentsOnClick() {
            @Override
            public void onClick(Uri uri) {
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("Downloading");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.pdf");

                DownloadManager downloadManager = (DownloadManager) DocumentFragment.this.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_document, container, false);

        ButterKnife.bind(this , view);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent , SELECT_DOCUMENT_REQUEST_CODE );
            }
        });

        documentAdapter = new DocumentAdapter(this.getContext() , new ArrayList<Uri>() , new ArrayList<String>() , documentsOnClick);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(documentAdapter);


        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_DOCUMENT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                uploadData(uri);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateAdapter(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadData(final Uri uri){

        final ProgressDialog progressDialog = ProgressDialog.show(this.getContext() , "Uploading Document" , "Streaming data " ,
                false);
        progressDialog.setMax(100);

        storageReference = firebaseStorage.getReference(firebaseUser.getUid() +"/documents/" + Util.getNameFromUri(this.getContext() , uri));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(DocumentFragment.this.getContext() , "Success" , Toast.LENGTH_SHORT).show();

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri1) {
                        databaseReference.child(Util.getNameFromUri(DocumentFragment.this.getContext() , uri)).setValue(uri1.toString());
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long totalData = taskSnapshot.getTotalByteCount();
                long dataTransferred = taskSnapshot.getBytesTransferred();
                progressDialog.setProgress((int)(100*(dataTransferred/totalData)));
            }
        });



    }

    private void updateAdapter(DataSnapshot dataSnapshot){

        ArrayList<Uri> uriArrayList = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();

        for (DataSnapshot snapShot:
             dataSnapshot.getChildren()) {

            uriArrayList.add(Uri.parse(snapShot.getValue().toString()));
            name.add(snapShot.getKey());

        }

        documentAdapter.swapData(uriArrayList , name);
        documentAdapter.notifyDataSetChanged();


    }
}
