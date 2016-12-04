package Models;

/**
 * Created by Akash on 28-11-2016.
 */

public class FriendsModel
{
    String name, id,doj,last_seen,prof_pic;

    public FriendsModel(String name, String id, String doj, String last_seen, String prof_pic) {
        this.name = name;
        this.id = id;
        this.doj = doj;
        this.last_seen = last_seen;
        this.prof_pic = prof_pic;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDoj() {
        return doj;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public String getProf_pic() {
        return prof_pic;
    }
}
