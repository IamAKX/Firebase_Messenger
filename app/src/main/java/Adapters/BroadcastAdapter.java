package Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akash.applications.firebasemessenger.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Models.BroadcastModel;
import Models.BroadcastSelectedList;
import Models.FriendsModel;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Akash on 28-11-2016.
 */

public class BroadcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<BroadcastModel> list= new ArrayList<>();

    public BroadcastAdapter(Context context, ArrayList<BroadcastModel> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_broadcast, parent, false);
        return new BroadcastViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BroadcastViewHolder _holder = (BroadcastViewHolder)holder;
        final BroadcastModel tempModel = list.get(position);
        _holder.friendname.setText(tempModel.getName());
        _holder.checkBox.setOnCheckedChangeListener(null);
        _holder.checkBox.setChecked(tempModel.getSelected());

        String image_uri = context.getString(R.string.profileLink)+tempModel.getProfimg();
        Glide.with(context)
                .load(image_uri)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.loading)
                .error(R.drawable.failedimageload)
                .into(_holder.profpic);


        _holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tempModel.setSelected(true);
                    BroadcastSelectedList.addFriend(tempModel.getId(),tempModel.getName());
                }
                else
                {
                    tempModel.setSelected(true);
                    BroadcastSelectedList.deleteFriend(tempModel.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class BroadcastViewHolder extends RecyclerView.ViewHolder {
        TextView friendname;
        CheckBox checkBox;
        ImageView profpic;
        public BroadcastViewHolder(View item) {
            super(item);
            friendname = (TextView)item.findViewById(R.id.item_broadcast_name);
            checkBox = (CheckBox)item.findViewById(R.id.item_broadcast_checkbox);
            profpic = (ImageView)item.findViewById(R.id.item_broadcast_profpic);

        }
    }
}
