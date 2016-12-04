package Models;

/**
 * Created by Akash on 30-11-2016.
 */

public class AllUserListModel {
    String uid,name,token,joindate,lastseen,profpic;

    public AllUserListModel(String uid, String name, String token, String joindate, String lastseen, String profpic) {
        this.uid = uid;
        this.name = name;
        this.token = token;
        this.joindate = joindate;
        this.lastseen = lastseen;
        this.profpic = profpic;
    }

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getJoindate() {
        return joindate;
    }

    public String getLastseen() {
        return lastseen;
    }

    public String getProfpic() {
        return profpic;
    }
}
