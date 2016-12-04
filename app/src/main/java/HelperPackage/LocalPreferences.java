package HelperPackage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

import Models.AllUserListModel;

/**
 * Created by Akash on 27-11-2016.
 */

public class LocalPreferences {

    public static ArrayList<AllUserListModel> allUserListModelArrayList = new ArrayList<>();

    public static String[] getUserNameArray()
    {
        String[] array = new String[allUserListModelArrayList.size()];
        int i=0;
        for(AllUserListModel obj : allUserListModelArrayList)
        {
            array[i++] = obj.getUserName();
        }
        return array;
    }

    public static String getFriendsProfileImage(String id)
    {
        String name = "";
        for(AllUserListModel obj : allUserListModelArrayList)
        {
            if(obj.getUid().equalsIgnoreCase(id))
                name = obj.getProfpic();
        }
        return name;
    }


    public static void setLogginState(Context context, boolean state)
    {
        SharedPreferences sp = context.getSharedPreferences("LoginState", Activity.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = sp.edit();
        sEdit.putBoolean("state",state);
        sEdit.commit();
    }
    public static boolean getLogginState(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("LoginState", Activity.MODE_PRIVATE);
        return  sp.getBoolean("state",false);
    }

    public static void setToken(Context context, String token)
    {
        SharedPreferences sp = context.getSharedPreferences("fcm_token", Activity.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = sp.edit();
        sEdit.putString("token",token);
        sEdit.commit();
    }
    public static String getToken(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("fcm_token", Activity.MODE_PRIVATE);
        return  sp.getString("token"," ");
    }

    public static void setUserID(Context context, String ID)
    {
        SharedPreferences sp = context.getSharedPreferences("userid", Activity.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = sp.edit();
        sEdit.putString("id",ID);
        sEdit.commit();
    }

    public static String getID(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("userid", Activity.MODE_PRIVATE);
        return  sp.getString("id",null);
    }

    public static void setUserName(Context context, String name)
    {
        SharedPreferences sp = context.getSharedPreferences("username", Activity.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = sp.edit();
        sEdit.putString("name",name);
        sEdit.commit();
    }
    public static String getUserName(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("username", Activity.MODE_PRIVATE);
        return  sp.getString("name",null);
    }


}
