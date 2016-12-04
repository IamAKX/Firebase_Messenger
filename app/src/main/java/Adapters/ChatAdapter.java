package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akash.applications.firebasemessenger.BroadcastMessage;
import com.akash.applications.firebasemessenger.MainActivity;
import com.akash.applications.firebasemessenger.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

import HelperPackage.LocalPreferences;
import HelperPackage.TimeManager;
import LocalDatabase.MessageTracker;
import Models.ConversationModel;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Akash on 02-12-2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ConversationModel> list= new ArrayList<>();
    Context context;


    public ChatAdapter(ArrayList<ConversationModel> list, Context context) {
        this.list = list;
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ChatsViewHolder _holder = (ChatsViewHolder)holder;
        final ConversationModel tempModel = list.get(position);
        _holder.name.setText(tempModel.getCon_unname());
        if(tempModel.getCon_message().length()>20)
        {
            _holder.message.setText(tempModel.getCon_message().substring(0,17)+"...");
        }
        else
        {
            _holder.message.setText(tempModel.getCon_message());
        }

        Date startDate = TimeManager.stringToDate(tempModel.getCon_datetime());
        Date stopDate = TimeManager.stringToDate(TimeManager.timeNow());
        _holder.arrivedtime.setText(TimeManager.dateDifference(startDate,stopDate));

        if(tempModel.getCon_unname().substring(0,tempModel.getCon_unname().indexOf(' ')).equalsIgnoreCase("broadcast"))
        {
            _holder.profpic.setImageResource(R.drawable.broadcast);
        }
        else
        {

            String image_uri = context.getString(R.string.profileLink)+ LocalPreferences.getFriendsProfileImage(tempModel.getCon_uid());
            Glide.with(context)
                    .load(image_uri)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.failedimageload)
                    .into(_holder.profpic);
        }

        if(tempModel.getCon_seen().equalsIgnoreCase("0"))
        {
            _holder.name.setTypeface(null, Typeface.BOLD);
            _holder.message.setTypeface(null, Typeface.BOLD);
            _holder.arrivedtime.setTypeface(null, Typeface.BOLD);

        }

        _holder.fulllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = tempModel.getCon_id();
                new MessageTracker(context).updateSeenState(Integer.parseInt(id));

                Intent intent = new Intent(v.getContext(), BroadcastMessage.class);
                intent.putExtra("friend_name",tempModel.getCon_unname().substring(1+tempModel.getCon_unname().indexOf(':')));
                intent.putExtra("friend_id",tempModel.getCon_uid());
                intent.putExtra("message",tempModel.getCon_message());
                list.clear();
                list = new MessageTracker(context).getAllConversations();
                notifyItemChanged(position);
                notifyItemRangeChanged(position,getItemCount());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ChatsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout fulllayout;
        ImageView profpic;
        TextView name,arrivedtime;
        EmojiconTextView message;
        public ChatsViewHolder(View item) {
            super(item);
            fulllayout = (LinearLayout)item.findViewById(R.id.item_chat_layout);
            profpic = (ImageView)item.findViewById(R.id.item_chat_image);
            name = (TextView)item.findViewById(R.id.item_chat_name);
            message = (EmojiconTextView) item.findViewById(R.id.item_chat_message);
            arrivedtime = (TextView)item.findViewById(R.id.item_chat_time);


            fulllayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu menu = new PopupMenu(context, fulllayout);
                    MenuInflater inflater = menu.getMenuInflater();
                    inflater.inflate(R.menu.delete_chat_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String id = list.get(getAdapterPosition()).getCon_id();
                            new MessageTracker(context).deleteConversation(Integer.parseInt(id));
                            list.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(),getItemCount());
                            return false;
                        }
                    });

                    MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), fulllayout);
                    menuHelper.setForceShowIcon(true);

                    menu.show();
                    return false;
                }
            });
        }
    }

}
