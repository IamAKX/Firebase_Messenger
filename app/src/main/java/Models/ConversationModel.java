package Models;

/**
 * Created by Akash on 01-12-2016.
 */

public class ConversationModel {
    String con_id,con_uid,con_unname,con_message,con_datetime,con_seen;

    public ConversationModel(String con_id, String con_uid, String con_unname, String con_message, String con_datetime, String con_seen) {
        this.con_id = con_id;
        this.con_uid = con_uid;
        this.con_unname = con_unname;
        this.con_message = con_message;
        this.con_datetime = con_datetime;
        this.con_seen = con_seen;
    }

    public String getCon_id() {
        return con_id;
    }

    public String getCon_uid() {
        return con_uid;
    }

    public String getCon_unname() {
        return con_unname;
    }

    public String getCon_message() {
        return con_message;
    }

    public String getCon_datetime() {
        return con_datetime;
    }

    public String getCon_seen() {
        return con_seen;
    }

    public void setCon_seen(String con_seen) {
        this.con_seen = con_seen;
    }
}
