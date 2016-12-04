package com.akash.applications.firebasemessenger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class BroadcastMessage extends AppCompatActivity {
    String id,name;
    EmojiconTextView message;
    TextView prompter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_message);

        message = (EmojiconTextView) findViewById(R.id.broadcastchat_message);
        prompter = (TextView)findViewById(R.id.broadcastchat_prompter);

        name =  getIntent().getStringExtra("friend_name");
        id =  getIntent().getStringExtra("friend_id");

        getSupportActionBar().setTitle("Broadcast - "+name.toUpperCase());
        getSupportActionBar().setIcon(R.drawable.broadcast);
        message.setText(getIntent().getStringExtra("message"));

        prompter.setText("Broadcast message from "+name+". Tap to reply "+name);
        prompter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PrivateChat.class);
                intent.putExtra("friend_name",name);
                intent.putExtra("friend_id",id);
                startActivity(intent);
                finish();
            }
        });
    }
}
