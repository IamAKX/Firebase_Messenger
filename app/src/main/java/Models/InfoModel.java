package Models;

/**
 * Created by Akash on 02-12-2016.
 */

public class InfoModel {

    String status, time, joindate,active;

    public InfoModel(String status, String time, String joindate, String active) {
        this.status = status;
        this.time = time;
        this.joindate = joindate;
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getJoindate() {
        return joindate;
    }

    public String getActive() {
        return active;
    }
}
