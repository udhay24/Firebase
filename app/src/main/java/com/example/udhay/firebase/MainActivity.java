package com.example.udhay.firebase;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.udhay.firebase.Adapters.ViewPagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //Firebase Instances

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //View instances
    @BindView(R.id.fragment_view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tablayout;

    //Request code
    private static final int SIGN_IN_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ButterKnife.bind(this);

        if(firebaseUser == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                    .setIsSmartLockEnabled(false)
                    .build(), SIGN_IN_REQUEST_CODE);
        }else{
            Toast.makeText(this , "Sign In Successful" , Toast.LENGTH_SHORT).show();
            setUpScreenView();
        }
    }


    private void setUpScreenView(){

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
    }
}
