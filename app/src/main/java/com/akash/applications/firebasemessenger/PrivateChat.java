package com.akash.applications.firebasemessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import Connection.ConnectorClass;

public class PrivateChat extends AppCompatActivity {
    String name,id;
    ListView listView;
    EditText input;
    ImageView sendButton,emojiButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        name =  getIntent().getStringExtra("friend_name");
        id =  getIntent().getStringExtra("friend_id");
        getSupportActionBar().setTitle(name);

        listView = (ListView)findViewById(R.id.privatechat_chat_list);
        input = (EditText)findViewById(R.id.privatechat_input_message);
        sendButton = (ImageView) findViewById(R.id.privatechat_send);
        emojiButton = (ImageView) findViewById(R.id.privatechat_emoticon);
        final ConnectorClass cc = new ConnectorClass(getBaseContext());
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cc.isConnectingToInternet())
                {
                    cc.showSnackBar(v,"No internet connection!");
                }
                input.setText("");
            }
        });
    }
}
