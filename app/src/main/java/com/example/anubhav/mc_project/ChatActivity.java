package com.example.anubhav.mc_project;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.Chat;
import com.example.anubhav.mc_project.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private EditText message;
    private RecyclerView rv;

    private DatabaseReference root;
    private ChatMessageAdapter adapter;

    private String event_creator_id;
    private Chat sessionChat = new Chat();

    private boolean nodeFound = false;
    private String foundNodeKey;

    private ArrayList<Message> messages = sessionChat.getMessages();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        event_creator_id = getIntent().getStringExtra("event_creator_id");

        this.setTitle(event_creator_id);

        message = (EditText) findViewById(R.id.msg_input);
        rv = (RecyclerView) findViewById(R.id.chatPageRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        assert mUser != null;
        adapter = new ChatMessageAdapter(messages,mUser.getUid());
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());

        FirebaseDatabase.getInstance().getReference().child("chat")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chat chat = snapshot.getValue(Chat.class);
                            if( (chat.getSender().equals(mUser.getUid()) && chat.getReceiver().equals(event_creator_id)) || (chat.getReceiver().equals(mUser.getUid()) && chat.getSender().equals(event_creator_id))) {
//                                Toast.makeText(PrivateChatActivity.this, "Got Node", Toast.LENGTH_SHORT).show();
                                sessionChat = chat;
                                messages.clear();
                                messages.addAll(sessionChat.getMessages());
                                adapter.notifyDataSetChanged();
//                                Toast.makeText(PrivateChatActivity.this, String.valueOf(messages.get(0).getMessage()), Toast.LENGTH_SHORT).show();
                                nodeFound = true;
                                foundNodeKey = snapshot.getKey();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void sendChat(View view) {
        final String messageText = message.getText().toString();
        if(!messageText.isEmpty()) {
            Calendar c = Calendar.getInstance();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String formattedDate = df.format(c.getTime());
            final Message newMessage = new Message(messageText, mUser.getUid(), formattedDate);
//            chatObject.add(new ChatMessage(messageText, f
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(nodeFound) {
                        sessionChat.addMessages(newMessage);
                        messages.clear();
//                        for(Message m : sessionChat.getMessages()) {
//                            messages.add(m);
//                            adapter.notifyDataSetChanged();
//                        }
                        messages.addAll(sessionChat.getMessages());
                        adapter.notifyDataSetChanged();
                        rootRef.child("chat").child(foundNodeKey).child("messages").setValue(sessionChat.getMessages());
                    }
                    else {
                        sessionChat = new Chat(mUser.getUid(),event_creator_id);
                        sessionChat.addMessages(newMessage);
                        messages.clear();
                        messages.addAll(sessionChat.getMessages());
                        adapter.notifyDataSetChanged();
                        foundNodeKey = rootRef.child("chat").push().getKey();
                        rootRef.child("chat").child(foundNodeKey).setValue(sessionChat);
                        nodeFound = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(ChatActivity.this, "Please Enter a Message", Toast.LENGTH_SHORT).show();
        }
    }
}
