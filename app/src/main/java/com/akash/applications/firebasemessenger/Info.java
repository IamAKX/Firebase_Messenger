package com.akash.applications.firebasemessenger;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.InfoAdapter;
import HelperPackage.LocalPreferences;
import Models.InfoModel;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Info extends Activity {
    private ArrayList<InfoModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private InfoAdapter adapter;
    ImageView userdp;
    String uid;
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info);


        uid = getIntent().getStringExtra("friend_id");
        String name = getIntent().getStringExtra("friend_name");
        String imageurl = getString(R.string.profileLink)+getIntent().getStringExtra("image_name");

        userdp = (ImageView)findViewById(R.id.headerImageView);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        appBarLayout.setMinimumHeight(deviceWidth);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);
        Glide.with(getBaseContext())
                .load(imageurl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.failedimageload)
                .into(userdp);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        new GetStatus().execute(); //adding data to array list




    }

    private class GetStatus extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"getstatus.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");
                            parseFromJson(response);
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
                    params.put("uid", uid);

                    return params;
                }
            };

            //Adding the string request to the queue
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            requestQueue.getCache().clear();

            requestQueue.add(stringRequest);
            return null;
        }
    }

    private void parseFromJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("statusdata");

                JSONObject data = result.getJSONObject(0);
                list.add(new InfoModel(data.getString("status"),data.getString("time"),data.getString("joindate"),data.getString("lastseen")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new InfoAdapter(getBaseContext(), list);
        recyclerView.setAdapter(adapter);

    }
}
