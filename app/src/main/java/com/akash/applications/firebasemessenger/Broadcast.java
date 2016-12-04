package com.akash.applications.firebasemessenger;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.BroadcastAdapter;
import HelperPackage.LocalPreferences;
import HelperPackage.TimeManager;
import Models.AllUserListModel;
import Models.BroadcastModel;
import Models.BroadcastSelectedList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Broadcast extends Fragment {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<BroadcastModel> BroadcastModelArrayList = new ArrayList<>();
    BroadcastAdapter broadcastAdapter;

    AutoCompleteTextView autoCompleteTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_broadcast, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)getView().findViewById(R.id.broadcast_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.broadcast_swipetorefresh);
        autoCompleteTextView = (AutoCompleteTextView)getView().findViewById(R.id.broadcast_autocompletetextview);


        //refreshAdapterProcedure();
        if(LocalPreferences.allUserListModelArrayList.size()==0)
            new LoadFriends().execute();
        else
            refreshAdapterProcedure();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item, LocalPreferences.getUserNameArray());

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LocalPreferences.allUserListModelArrayList.clear();
                        new LoadFriends().execute();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }, 3000);
            }
        });

        getView().findViewById(R.id.broadcast_button_createBroadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BroadcastSelectedList.hashMap.size()!=0)
                    startActivity(new Intent(getContext(),CreateBroadcast.class));
                else
                    Toast.makeText(getContext(),"No friend is selected",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refreshAdapterProcedure() {
        BroadcastModelArrayList.clear();

        fillData();
        broadcastAdapter = new BroadcastAdapter(getActivity(),BroadcastModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(broadcastAdapter);
        broadcastAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    private void fillData() {
        for(AllUserListModel obj : LocalPreferences.allUserListModelArrayList) {
            BroadcastModelArrayList.add(new BroadcastModel(obj.getUserName(), obj.getUid(),obj.getProfpic(), false));
        }
    }

    private class LoadFriends extends AsyncTask<Void,Void,Void> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading friend lists");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(dialog.isShowing())
                dialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"fetchusers.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");
                            parseUsersFromJson(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            Log.i("Checking",error+" ");
                        }
                    }){

            };

            //Adding the string request to the queue
            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.getCache().clear();

            requestQueue.add(stringRequest);
            return null;
        }
    }

    private void parseUsersFromJson(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("userdata");

            if (result == null) {
                Toast.makeText(getContext(), "No Friend!!", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < result.length(); i++) {
                JSONObject data = result.getJSONObject(i);
                LocalPreferences.allUserListModelArrayList.add(new AllUserListModel(data.getString("id"),
                        data.getString("name"),
                        data.getString("token"),
                        TimeManager.parseJoiningDate(data.getString("joindate")),
                        data.getString("lastseen"),
                        data.getString("profpic")
                ));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        refreshAdapterProcedure();
    }

}
