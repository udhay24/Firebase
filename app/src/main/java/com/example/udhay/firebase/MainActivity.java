package com.example.udhay.firebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.udhay.firebase.Adapters.ViewPagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    //Firebase Instances

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //View instances
    @Nullable @BindView(R.id.fragment_view_pager) ViewPager viewPager;

    @Nullable @BindView(R.id.tab_layout) TabLayout tablayout;

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;

    //Request code
    private static final int SIGN_IN_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ButterKnife.bind(this);

        Timber.plant(new Timber.DebugTree());
        setSupportActionBar(toolbar);

        if(firebaseUser == null){
            signInScreen();
        }else{
            Toast.makeText(this , "Sign In Successful" , Toast.LENGTH_SHORT).show();
            setUpScreenView();
        }

        if(ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);

        }
    }


    private void setUpScreenView(){

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu , menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.log_out:
                firebaseAuth.signOut();
                signInScreen();
                Toast.makeText(this, "Successfully Signed Out" , Toast.LENGTH_SHORT).show();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    private void signInScreen(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(), SIGN_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == SIGN_IN_REQUEST_CODE){
            if(requestCode == RESULT_OK){
                setUpScreenView();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
