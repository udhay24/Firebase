package com.example.udhay.firebase;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextRecognitionFragment extends Fragment {

    private static final int PHOTO_REQUEST_CODE = 100;

   Uri photoURI;

    @BindView(R.id.recognized_text)
    TextView textView;

    @BindView(R.id.recognise_button)
    Button button;

    @BindView(R.id.imageView)
    ImageView imageView;


    public TextRecognitionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_recognition, container, false);;

        ButterKnife.bind(this , view);

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Crashlytics.getInstance().crash();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = new File(TextRecognitionFragment.this.getContext().getFilesDir() , "images");

                File imageFile = new File(file , java.util.Calendar.getInstance().getTime().toString()+".jpg");

                photoURI = FileProvider.getUriForFile(TextRecognitionFragment.this.getContext() , "com.example.udhay.firebase.fileprovider" , imageFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT , photoURI);

                startActivityForResult(intent , PHOTO_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == PHOTO_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                textRecogniser(photoURI);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void textRecogniser(Uri photoUri){

        FirebaseVisionImage firebaseVisionImage = null;

        try {
            firebaseVisionImage = FirebaseVisionImage.fromFilePath(TextRecognitionFragment.this.getContext(), photoUri);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setText(firebaseVisionText.getText());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TextRecognitionFragment.this.getContext(), "unable to recognise Image", Toast.LENGTH_SHORT).show();
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                Picasso.get().load(photoURI).into(imageView);
            }
        });
    }

}
