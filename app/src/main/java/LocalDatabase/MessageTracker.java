package LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import Models.ConversationModel;

/**
 * Created by Akash on 01-12-2016.
 */

public class MessageTracker extends SQLiteOpenHelper {

    private static final String DB_NAME = "ConversationDatabase";
    private static final String TABLE_NAME = "message";
    private static final String ID = "id";
    private static final String COL_USER_ID = "uid";
    private static final String COL_NAME = "name";
    private static final String COL_Message = "message";
    private static final String COL_TIME = "rec_time";
    private static final String COL_SEEN = "seen";

    public MessageTracker(Context context) {
        super(context,DB_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+TABLE_NAME +
                        " ("+ID+" integer primary key autoincrement, "+COL_USER_ID+" text,"+COL_NAME+" text,"+COL_Message+" text, "+COL_TIME+" text, "+COL_SEEN+" text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMessage (String uid, String name, String message, String datetime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, uid);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_Message, message);
        contentValues.put(COL_TIME, datetime);
        contentValues.put(COL_SEEN, "0");
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME );
        return numRows;
    }

    public boolean updateSeenState (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SEEN,"1");

        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }


    public Integer deleteConversation (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ConversationModel> getAllConversations() {
        ArrayList<ConversationModel> array_list = new ArrayList<ConversationModel>();
        String cid,uid,name,msg,rev_time,see;
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" order by "+COL_TIME+" desc", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            cid=res.getString(res.getColumnIndex(ID));
            uid=res.getString(res.getColumnIndex(COL_USER_ID));
            name=res.getString(res.getColumnIndex(COL_NAME));
            msg=res.getString(res.getColumnIndex(COL_Message));
            rev_time=res.getString(res.getColumnIndex(COL_TIME));
            see =res.getString(res.getColumnIndex(COL_SEEN));
            array_list.add(new ConversationModel(cid,uid,name,msg,rev_time,see));
            res.moveToNext();
        }
        return array_list;
    }

}
