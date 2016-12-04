package com.akash.applications.firebasemessenger;


import android.app.ProgressDialog;
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

import Adapters.FriendsAdapter;
import HelperPackage.LocalPreferences;
import HelperPackage.TimeManager;
import Models.AllUserListModel;
import Models.FriendsModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class Friends extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<FriendsModel> friendsModelArrayList = new ArrayList<>();
    FriendsAdapter friendsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)getView().findViewById(R.id.friend_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.friend_swipetorefresh);

        if(LocalPreferences.allUserListModelArrayList.size()==0)
            new LoadFriends().execute();
        else
            refreshAdapterProcedure();

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
    }

    private void refreshAdapterProcedure() {
        friendsModelArrayList.clear();

        fillData();
        friendsAdapter = new FriendsAdapter(getActivity(),friendsModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(friendsAdapter);
        friendsAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    private void fillData() {
        for(AllUserListModel obj : LocalPreferences.allUserListModelArrayList)
        {
            friendsModelArrayList.add(new FriendsModel(obj.getUserName(),obj.getUid(),obj.getJoindate(),obj.getLastseen(),obj.getProfpic()));
        }

    }


    private class LoadFriends extends AsyncTask<Void,Void,Void>{
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading friend lists");
            dialog.setCancelable(true);
            dialog.show();
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
                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            Log.i("Checking",error+" ");
                            if(dialog.isShowing())
                                dialog.dismiss();
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
