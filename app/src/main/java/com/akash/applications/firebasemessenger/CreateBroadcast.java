package com.akash.applications.firebasemessenger;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.CreateBroadcastAdapter;
import Connection.ConnectorClass;
import HelperPackage.LocalPreferences;
import Models.AllUserListModel;
import Models.BroadcastSelectedList;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CreateBroadcast extends AppCompatActivity {


    EmojiconEditText inputMessage;
    ImageView emoticonBtn,sendBtn;
    View rootView;
    EmojIconActions emojIconActions;
    Context context;
    RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<AllUserListModel> list = new ArrayList<>();
    private HashMap<String,String> hm;

    String uid_list="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_broadcast);

        getSupportActionBar().setTitle("New Broadcast");

        hm = new HashMap<>();
        hm = BroadcastSelectedList.hashMap;

        //friendList = (TextView)findViewById(R.id.newbroadcast_friendlist);
        inputMessage = (EmojiconEditText) findViewById(R.id.newbroadcast_input_message);
        emoticonBtn = (ImageView) findViewById(R.id.newbroadcast_emoticon);
        sendBtn = (ImageView)findViewById(R.id.newbroadcast_send);
        rootView = findViewById(R.id.activity_create_broadcast);

        context = getApplicationContext();
        relativeLayout = (RelativeLayout)findViewById(R.id.item_newbroadcast_relativelayout);
        recyclerView = (RecyclerView)findViewById(R.id.newbroadcast_recyclerview);
        fillArrayListItems();
        layoutManager = new GridLayoutManager(context,4);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CreateBroadcastAdapter(list, context);
        recyclerView.setAdapter(adapter);

        emojIconActions = new EmojIconActions(this,rootView,emoticonBtn,inputMessage);
        emojIconActions.ShowEmojicon();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectorClass cc = new ConnectorClass(getBaseContext());
                if(cc.isConnectingToInternet())
                {

                    for (String key : hm.keySet()) {
                        uid_list+=key+", ";
                    }
                    uid_list=uid_list.trim();
                    if(uid_list.length()<1)
                    {
                        Toast.makeText(getApplication(),"No friend selected",Toast.LENGTH_LONG).show();
                        return;
                    }
                    uid_list = uid_list.substring(0,uid_list.length()-1);
                    uid_list = "("+uid_list+")";
                    if(inputMessage.getText().toString().equals(""))
                    {
                        inputMessage.setError("Message box is empty");
                        return;
                    }
                    final String msg = inputMessage.getText().toString().trim();
                    inputMessage.setText("");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"broadcast.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //If we are getting success from server
                                    Log.i("Checking",response+" ");
                                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //You can handle error here if you want
                                    Log.i("Checking",error+" ");
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //Adding parameters to request
                            params.put("uid", uid_list);
                            params.put("message",msg);
                            params.put("title","New broadcast from "+LocalPreferences.getUserName(getBaseContext()));
                            return params;

                        }
                    };

                    //Adding the string request to the queue
                    stringRequest.setShouldCache(false);

                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    requestQueue.getCache().clear();

                    requestQueue.add(stringRequest);


                }

                else
                {
                    cc.showSnackBar(getWindow().getDecorView(),"Failed to send, No internet connection");
                }
            }
        });


    }

    private void fillArrayListItems() {
        for(AllUserListModel obj : LocalPreferences.allUserListModelArrayList) {
            for (String key : hm.keySet()) {
                if (key.equalsIgnoreCase(obj.getUid()))
                    list.add(obj);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_broadcast_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        switch(id)
        {
            case R.id.add:
                    finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
