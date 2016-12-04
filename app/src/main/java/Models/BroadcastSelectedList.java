package Models;

import java.util.HashMap;

/**
 * Created by Akash on 28-11-2016.
 */

public class BroadcastSelectedList {

    public static HashMap<String,String> hashMap = new HashMap<>();

    public static void addFriend(String id,String name)
    {
        hashMap.put(id,name);
    }

    public static void deleteFriend(String id)
    {
        hashMap.remove(id);
    }
}
