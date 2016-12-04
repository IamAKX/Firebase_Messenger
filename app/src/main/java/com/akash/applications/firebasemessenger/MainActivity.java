package com.akash.applications.firebasemessenger;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import Connection.ConnectorClass;
import HelperPackage.LocalPreferences;
import HelperPackage.TimeManager;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    String FCM_TOKEN=" ";
    String USER_NAME=null;
    EditText input;
    private ConnectorClass cc;
    private int deviceWidth = 120;
    private AlertDialog dpDialogBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;

        startService(new Intent(MainActivity.this, FcmInstanceIdService.class));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        cc = new ConnectorClass(getBaseContext());
        if(!cc.isConnectingToInternet())
            cc.showSnackBar(getWindow().getDecorView(),"No Internect connection");
        FCM_TOKEN = LocalPreferences.getToken(getApplicationContext());
          if(!LocalPreferences.getLogginState(getBaseContext()))
            {
                showPromptDialogbox();
            }
        else
          {
              new UpdateSeenState("online").execute();
          }

    }


    private void showPromptDialogbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Registeration");
        builder.setMessage("Enter your name to register or login");
        input = new EditText(MainActivity.this);
        input.setHint("Enter username");
        builder.setView(input);
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!cc.isConnectingToInternet())
                {
                    cc.showSnackBar(getWindow().getDecorView(),"Check your internet connection");
                    return;
                }

                //send registration request
                //set login state to true
                //generate Firebase Token
                USER_NAME = input.getText().toString();
                input.setText("");
                new RegisterNewUser().execute();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //send login request
                //set login state to true
                //generate Firebase Token
                if(!cc.isConnectingToInternet())
                {
                    cc.showSnackBar(getWindow().getDecorView(),"Check your internet connection");
                    return;
                }

                USER_NAME = input.getText().toString();
                input.setText("");
                new LoginUser().execute();

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id)
        {
            case R.id.logout:
                LocalPreferences.setLogginState(getBaseContext(),false);
                new UpdateSeenState(TimeManager.timeNow()).execute();
                startActivity(new Intent(getBaseContext(),MainActivity.class));
                finish();
                return true;
            case R.id.share:
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Download FireBase Messenger app now!!");
                i.putExtra(Intent.EXTRA_TEXT, "\n"+getString(R.string.serverLink)+"android/download.php\n\nShared via Firebase Messenger.");
                startActivity(Intent.createChooser(i, "Share via.."));
                return true;
            case R.id.changedp:
                //Toast.makeText(getBaseContext(),"This feature is yet to come",Toast.LENGTH_LONG).show();
                showChangeProfilePictureDialog();
                return true;
            case R.id.changestatus:
                if(cc.isConnectingToInternet())
                    changeStatusPrompt();
                else
                    cc.showSnackBar(getWindow().getDecorView(),"No internet connection!!");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangeProfilePictureDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.update_profile_pic_layout, null);

        ImageView camerabtn = (ImageView)alertLayout.findViewById(R.id.camera_btn);
        ImageView gallerybtn = (ImageView)alertLayout.findViewById(R.id.gallery_btn);

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE },PERMISSION_REQ_CAMERA);
                }
                else
                    launchCamera();

                dpDialogBox.dismiss();
            }
        });

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    launchGallery();
                    dpDialogBox.dismiss();
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_STORAGE_WRITE);
                }
            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert
                .setView(alertLayout)
                .setCancelable(true);



        dpDialogBox = alert.create();
        dpDialogBox.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
                }
        }
        else
            if(requestCode == PERMISSION_REQ_STORAGE_WRITE){
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                    launchGallery();
            }
    }

    private void changeStatusPrompt() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.update_status_layout, null);
        final EditText inputStatus = (EditText) alertLayout.findViewById(R.id.updatestatus_input_message);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Update Status")
                .setMessage("\n")
                .setView(alertLayout)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String statusMsg = inputStatus.getText().toString();
                        new UpdateStatus(statusMsg).execute();
                        inputStatus.setText("");
                    }
                });
                AlertDialog dialogBox = alert.create();
                dialogBox.show();

    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new Friends();
                case 1:
                    return new Chat();
                case 2:
                    return new Broadcast();
                default:
                    return new Chat();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Friends";
                case 1:
                    return "Chats";
                case 2:
                    return "Broadcast";
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new UpdateSeenState(TimeManager.timeNow()).execute();
    }

    private class RegisterNewUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"register.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");
                            if(response.trim().equalsIgnoreCase("failed"))
                            {
                                showPromptDialogbox();
                                input.setError("Try another user name");
                            }
                            else
                            {
                                String id = response.substring(0,response.indexOf("#"));
                                if(!id.equals(""))
                                {
                                    LocalPreferences.setUserID(getBaseContext(),response.substring(0,response.indexOf("#")));
                                    LocalPreferences.setUserName(getBaseContext(),USER_NAME);
                                    LocalPreferences.setLogginState(getBaseContext(),true);
                                    Toast.makeText(getBaseContext(),"Registered Successfully!!\n Swipe down to refresh.",Toast.LENGTH_LONG).show();

                                    updateTokenOnServer();
                                    startService(new Intent(MainActivity.this, FcmInstanceIdService.class));
                                }
                                Toast.makeText(getBaseContext(),response.substring(response.indexOf("#")+1),Toast.LENGTH_LONG);

                                new UpdateSeenState("online").execute();
                            }
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
                    params.put("name",USER_NAME);
                    params.put("token",FCM_TOKEN);
                    params.put("date", TimeManager.dateToday());
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

    private void updateTokenOnServer() {
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
                params.put("uid",LocalPreferences.getID(getBaseContext()));
                params.put("token",LocalPreferences.getToken(getBaseContext()));

                return params;
            }
        };

        //Adding the string request to the queue
        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);
    }

    private class LoginUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"login.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");
                            if(response.trim().equalsIgnoreCase("failed"))
                            {
                                showPromptDialogbox();
                                input.setError("Enter correct user name");
                            }
                            else
                            {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray("userdata");
                                    JSONObject data = result.getJSONObject(0);
                                    LocalPreferences.setUserID(getBaseContext(),data.getString("id"));
                                    LocalPreferences.setUserName(getBaseContext(),data.getString("name"));
                                    LocalPreferences.setToken(getBaseContext(),data.getString("token"));
                                    LocalPreferences.setLogginState(getBaseContext(),true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new UpdateSeenState("online").execute();
                                Toast.makeText(getBaseContext(),"Welcome back "+LocalPreferences.getUserName(getBaseContext()),Toast.LENGTH_LONG).show();
                                startService(new Intent(MainActivity.this, FcmInstanceIdService.class));
                            }
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
                    params.put("name",USER_NAME);

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

    private class UpdateSeenState extends AsyncTask<Void,Void,Void>{
        String seentime;

        public UpdateSeenState(String seentime) {
            this.seentime = seentime;
        }

        @Override
        protected Void doInBackground(Void... params) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"updatseen.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");
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
                    params.put("id",LocalPreferences.getID(getBaseContext()));
                    params.put("seen",seentime);

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

    private class UpdateStatus extends AsyncTask<Void,Void,Void> {
        String statusMsg;

        public UpdateStatus(String statusMsg) {
            this.statusMsg = statusMsg;
        }

        @Override
        protected Void doInBackground(Void... params) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"updatestatus.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");

                            Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
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
                    params.put("id",LocalPreferences.getID(getBaseContext()));
                    params.put("status",statusMsg);
                    params.put("time",TimeManager.timeNow());

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


    //------------------------------------Update profile image----------------------------------------------------

    Uri profilePic=null;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PIC_CROP = 3;

    private static final int PERMISSION_REQ_CAMERA = 77 ;
    private static final int PERMISSION_REQ_STORAGE_WRITE = 88 ;
    private static final int PERMISSION_REQ_STORAGE_REA = 99 ;
    public void launchCamera() {

        try{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String imgDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Firebase Messenger/Profile";
            File f = new File(imgDirectory);
            if(!f.exists())
                f.mkdirs();

            File imageFile = new File(imgDirectory+"/IMG_"+System.currentTimeMillis()+".jpg");
            profilePic = Uri.fromFile(imageFile); // convert path to Uri
            Log.i("Cropping",profilePic.getPath()+" ");
            Log.i("Cropping uri",profilePic+" ");
            takePictureIntent.putExtra( android.provider.MediaStore.EXTRA_OUTPUT, profilePic );
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);

        }

        catch (ActivityNotFoundException err)
        {
            Toast.makeText(getBaseContext(),"Camera Not available or Give the permission of the camera from the Settings manually", Toast.LENGTH_LONG).show();
        }
    }

    public void launchGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        try {

            intent.putExtra("return-data", true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Complete action using"), PICK_FROM_GALLERY);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(),"Unable to launch gallary",Toast.LENGTH_LONG).show();
        }
    }

    private void performCrop(Uri data) {

        try{

            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(data, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", deviceWidth);
            cropIntent.putExtra("outputY", deviceWidth);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch (ActivityNotFoundException err)
        {
            Log.i("Cropping not",data+" ");
            Toast.makeText(getBaseContext(),"Failed to crop image",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ) 
        {
           if(requestCode == PICK_FROM_CAMERA)
            {
                performCrop(profilePic);
            }
            else 
            if(requestCode == PIC_CROP)
               {
                    Bundle extras = data.getExtras();
                    Bitmap finalPic = extras.getParcelable("data");
                    String encryptedString = getStringImage(finalPic);
                    new ContactServer(encryptedString).execute();
               }
                else 
                    if(requestCode == PICK_FROM_GALLERY)
                    {
                        performCrop(data.getData());
                    }
        }
    }

    private String getStringImage(Bitmap finalPic) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        finalPic.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private class ContactServer extends AsyncTask<Void,Void,Void>{
        String encodedImage;

        public ContactServer(String encodedImage) {
            this.encodedImage = encodedImage;
        }

        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Update Profile Picture");
            dialog.setCancelable(true);
            dialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverLink)+"updateprofileimage.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If we are getting success from server
                            Log.i("Checking",response+" ");
                            if(dialog.isShowing())
                                dialog.dismiss();
                            Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put("id",LocalPreferences.getID(getBaseContext()));
                    params.put("img",encodedImage);

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
}

