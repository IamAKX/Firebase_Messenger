package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akash.applications.firebasemessenger.R;

import java.util.ArrayList;
import java.util.Date;

import HelperPackage.TimeManager;
import Models.InfoModel;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by Akash on 02-12-2016.
 */

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<InfoModel> list = new ArrayList<>();

    public InfoAdapter(Context context, ArrayList<InfoModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_info, parent, false);
        return new InfoViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InfoViewHolder _holder = (InfoViewHolder) holder;
        InfoModel tempModel = list.get(position);

        Date stopDate = TimeManager.stringToDate(TimeManager.timeNow());
        Date startDate = TimeManager.stringToDate(tempModel.getTime());
        _holder.status_time.setText(TimeManager.dateDifference(startDate,stopDate));
        _holder.status.setText(tempModel.getStatus());

        _holder.join.setText("Since "+TimeManager.parseJoiningDate(tempModel.getJoindate()));
        if(tempModel.getActive().equalsIgnoreCase("online"))
        {
            _holder.active.setText("I am online now!!");
            _holder.active.setTextColor(Color.GREEN);
        }
        else
        {
            startDate = TimeManager.stringToDate(tempModel.getActive());
            _holder.active.setText("Last seen "+TimeManager.dateDifference(startDate,stopDate));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class InfoViewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView status,active,join;
        TextView status_time;
        public InfoViewHolder(View item) {
            super(item);

            status = (EmojiconTextView)item.findViewById(R.id.info_status);
            active = (EmojiconTextView)item.findViewById(R.id.info_active);
            join = (EmojiconTextView)item.findViewById(R.id.info_joinde);
            status_time = (TextView)item.findViewById(R.id.info_status_time);
        }
    }
}
