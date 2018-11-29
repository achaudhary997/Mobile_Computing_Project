package com.example.anubhav.mc_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anubhav.mc_project.models.Message;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder>{

    private static ArrayList<Message> chats;
    private String sessionUsername;
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    public ChatMessageAdapter(ArrayList<Message> items, String username) {
        chats = items;
        sessionUsername = username;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message currentObject = chats.get(position);
        holder.messageTV.setText(currentObject.getMessage());
        holder.nameTV.setText(currentObject.getSender());
        holder.timeTV.setText(currentObject.getTime());
        if(currentObject.getSender().equals(sessionUsername)) {
            holder.mCardView.setCardBackgroundColor(Color.LTGRAY);
        }

        setFadeAnimation(holder.itemView);

    }

    private final static int FADE_DURATION = 400;

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView messageTV;
        public TextView nameTV;
        public TextView timeTV;

        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.chatItemCardView);
            messageTV = (TextView) v.findViewById(R.id.chatPageMessageTV);
            nameTV = (TextView) v.findViewById(R.id.chatMessageName);
            timeTV = (TextView) v.findViewById(R.id.chatPageTimeTextView);
//            mCardView.setOnClickListener((View.OnClickListener) this);
        }
    }
}