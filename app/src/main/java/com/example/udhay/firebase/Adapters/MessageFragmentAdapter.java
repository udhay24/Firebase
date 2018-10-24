package com.example.udhay.firebase.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udhay.firebase.R;

import java.util.ArrayList;


public class MessageFragmentAdapter extends RecyclerView.Adapter<MessageFragmentAdapter.MessageViewHolder> {

    private Context context;
    private ArrayList<String> messageList;



    public MessageFragmentAdapter(Context context , ArrayList<String>messages){
        this.context = context;
        messageList = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.message_view_holder , viewGroup , false);
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        messageViewHolder.getMessageTextView().setText(messageList.get(i));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public void swapData(ArrayList<String> list){
        messageList = list;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        ImageView userIcon;

        TextView messageTextView;

        public MessageViewHolder(View view){
            super(view);
            userIcon = view.findViewById(R.id.user_icon);
            messageTextView = view.findViewById(R.id.message_text_view);
        }

        public ImageView getUserIcon(){
            return userIcon;
        }

        public TextView getMessageTextView(){
            return messageTextView;
        }
    }
}
