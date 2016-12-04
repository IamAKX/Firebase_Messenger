package Models;

/**
 * Created by Akash on 28-11-2016.
 */

public class BroadcastModel
{
    String name, id, profimg;
    boolean selected;

    public BroadcastModel(String name, String id, String profimg, boolean selected) {
        this.name = name;
        this.id = id;
        this.profimg = profimg;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProfimg() {
        return profimg;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
