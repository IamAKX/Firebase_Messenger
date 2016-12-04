package com.akash.applications.firebasemessenger;

import android.util.Log;
import android.widget.Toast;

import com.akash.applications.firebasemessenger.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import HelperPackage.LocalPreferences;

/**
 * Created by Akash on 29-11-2016.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String recentToken = FirebaseInstanceId.getInstance().getToken();

        updateTokenOnServer(recentToken);
        LocalPreferences.setToken(getApplicationContext(),recentToken);
    }

    private void updateTokenOnServer(final String recentToken) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"updatetoken.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.i("Checking",response+" ");
                        Toast.makeText(getApplicationContext(),"Token update report : "+response,Toast.LENGTH_SHORT).show();
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
                params.put("uid",LocalPreferences.getID(getApplicationContext()));
                params.put("token",recentToken);

                return params;
            }
        };

        //Adding the string request to the queue
        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);
    }
}
