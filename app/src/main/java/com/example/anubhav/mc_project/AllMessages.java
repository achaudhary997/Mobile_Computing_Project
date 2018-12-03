package com.example.anubhav.mc_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anubhav.mc_project.models.Chat;
import com.example.anubhav.mc_project.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class AllMessages extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference root;
    private static final String TAG = "AllMessages";

    private ListView rv;

    private ArrayList<String> userList = new ArrayList<>();

    boolean isCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_all_messages);

        rv = (ListView) findViewById(R.id.listView);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        root = FirebaseDatabase.getInstance().getReference().getRoot().child(Helper.chatNode);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, userList);
        rv.setAdapter(arrayAdapter);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Fetching Your Chats...");
        pd.setCancelable(false);
        pd.show();

//        final Semaphore semaphore = new Semaphore(0);

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();
                    String currentUserId = currentUser.getUid();
                    if(currentUserId.equals(sender)) {
                        userList.add(receiver);
                    } else if (currentUserId.equals(receiver)) {
                        userList.add(sender);
                    }
                }
                if (userList.size() == 0) {
                    userList.add("No Messages Here.");
                }
                Toast.makeText(AllMessages.this, userList.toString(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
                isCompleted = true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                while (!isCompleted);
                Toast.makeText(AllMessages.this, "Executed", Toast.LENGTH_SHORT).show();
                arrayAdapter.notifyDataSetChanged();

            }
        }, 1000);

        rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = (String) rv.getItemAtPosition(position);
                Log.d(TAG, "onItemClick: " + position);
                if (!username.contains("No Messages")) {
                    Intent privateChat = new Intent(AllMessages.this, ChatActivity.class);
                    privateChat.putExtra("event_creator_id", username);
                    startActivity(privateChat);
                }
            }
        });
    }
}
