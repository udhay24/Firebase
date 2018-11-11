package com.example.udhay.firebase;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;

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

    @BindView(R.id.demoImageView)
    ImageView imageView;


    public TextRecognitionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_recognition, container, false);

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
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = new File(TextRecognitionFragment.this.getContext().getFilesDir(), "image.jpg");

                photoURI = FileProvider.getUriForFile(TextRecognitionFragment.this.getContext(),
                        "com.example.udhay.firebase.fileprovider",
                        file);

                intent.putExtra(MediaStore.EXTRA_OUTPUT , photoURI);

                startActivityForResult(intent , PHOTO_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PHOTO_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                getText();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getText() {

        FirebaseVisionImage firebaseVisionImage = null;
        try {
            firebaseVisionImage = FirebaseVisionImage.fromFilePath(getContext(), photoURI);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String message = firebaseVisionText.getText();
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setText(message);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "failed to  recognise image", Toast.LENGTH_SHORT).show();
            }
        });
    }

}