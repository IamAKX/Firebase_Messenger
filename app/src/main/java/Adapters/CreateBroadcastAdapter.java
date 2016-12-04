package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akash.applications.firebasemessenger.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import Models.AllUserListModel;
import Models.BroadcastSelectedList;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Akash on 02-12-2016.
 */

public class CreateBroadcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<AllUserListModel> list = new ArrayList<>();
    Context context;

    public CreateBroadcastAdapter(ArrayList<AllUserListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_newbroadcast, parent, false);
        return new NewBroadcastViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewBroadcastViewHolder _holder = (NewBroadcastViewHolder)holder;
        final AllUserListModel tempModel = list.get(position);
        String image_uri = context.getString(R.string.profileLink)+tempModel.getProfpic();
        Glide.with(context)
                .load(image_uri)
                .bitmapTransform(new CropCircleTransformation(context))
                .placeholder(R.drawable.loading)
                .error(R.drawable.failedimageload)
                .into(_holder.profic);
        _holder.profic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,tempModel.getUserName(),Toast.LENGTH_SHORT).show();
            }
        });
        _holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BroadcastSelectedList.deleteFriend(tempModel.getUid());

                try{
                    list.remove(position);
                }catch (Exception e){}
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class NewBroadcastViewHolder extends RecyclerView.ViewHolder {
        ImageView profic;
        ImageButton removeBtn;

        public NewBroadcastViewHolder(View item) {
            super(item);
            profic = (ImageView)item.findViewById(R.id.item_newbroadcast_profpiv);
            removeBtn = (ImageButton)item.findViewById(R.id.item_newbroadcast_remove);

        }
    }
}
