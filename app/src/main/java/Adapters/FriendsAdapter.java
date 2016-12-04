package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akash.applications.firebasemessenger.Info;
import com.akash.applications.firebasemessenger.PrivateChat;
import com.akash.applications.firebasemessenger.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

import HelperPackage.TimeManager;
import Models.FriendsModel;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Akash on 28-11-2016.
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<FriendsModel> list= new ArrayList<>();
    Context context;

    public FriendsAdapter( Context context, ArrayList<FriendsModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
        return new FriendsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendsViewHolder _holder = (FriendsViewHolder)holder;
        final FriendsModel tempModel = list.get(position);
        _holder.friendName.setText(tempModel.getName());

        if(tempModel.getLast_seen().equalsIgnoreCase("online"))
        {
            _holder.activedate.setText("Active now");
            _holder.activedate.setTextColor(Color.GREEN);
        }
        else
        {
            Date startDate = TimeManager.stringToDate(tempModel.getLast_seen());
            Date stopDate = TimeManager.stringToDate(TimeManager.timeNow());
            _holder.activedate.setText("Active "+TimeManager.dateDifference(startDate,stopDate));
        }

        String image_uri = context.getString(R.string.profileLink)+tempModel.getProf_pic();
        Glide.with(context)
                .load(image_uri)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.loading)
                .error(R.drawable.failedimageload)
                .into(_holder.profilePic);

        _holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Info.class);
                intent.putExtra("friend_id",tempModel.getId());
                intent.putExtra("friend_name",tempModel.getName());
                intent.putExtra("image_name",tempModel.getProf_pic());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView friendName,activedate;
        ImageView profilePic;
        public FriendsViewHolder(View item) {
            super(item);
            friendName = (TextView)item.findViewById(R.id.item_friend_name);
            activedate = (TextView)item.findViewById(R.id.item_friend_joindate);
            friendName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PrivateChat.class);
                    intent.putExtra("friend_name",list.get(getAdapterPosition()).getName());
                    intent.putExtra("friend_id",list.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(intent);
                }
            });
            profilePic = (ImageView)item.findViewById(R.id.item_friend_profilepic);
        }
    }
}
