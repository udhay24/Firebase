package com.example.udhay.firebase;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.udhay.firebase.Adapters.MessageFragmentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    //Firebase instances
    FirebaseAuth firebaseauth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Fragment Views
    @BindView(R.id.message_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.input_edit_text)
    EditText messageEditText;

    @BindView(R.id.send_message)
    Button sendMessage;

    //RecyclerView Instances
    MessageFragmentAdapter messageFragmentAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);;

        ButterKnife.bind(this , view);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        recyclerView.setAdapter(messageFragmentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateAdapter(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Not able to retrieve message", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseauth = FirebaseAuth.getInstance();
        firebaseUser = firebaseauth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(firebaseUser.getUid());

        messageFragmentAdapter = new MessageFragmentAdapter(this.getContext() , new ArrayList<String>());


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void sendMessage(){
        String message = messageEditText.getEditableText().toString();

        if(message.isEmpty()){
            Toast.makeText(this.getContext() , "Cannot send Blank Messages" , Toast.LENGTH_SHORT).show();
        }else{
            databaseReference.child(Calendar.getInstance().getTimeInMillis() +"").setValue(message);
            messageEditText.setText("");
            messageFragmentAdapter.notifyDataSetChanged();
        }
    }


    private void updateAdapter(DataSnapshot dataSnapshot){

        ArrayList<String> message = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
            message.add(snapshot.getValue().toString());
        }

        messageFragmentAdapter.swapData(message);
        messageFragmentAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageFragmentAdapter.getItemCount()-1);
    }
}
