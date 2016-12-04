package com.akash.applications.firebasemessenger;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapters.ChatAdapter;
import LocalDatabase.MessageTracker;
import Models.ConversationModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ConversationModel> conversationModels;
    MessageTracker tracker;
    ChatAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView)getView().findViewById(R.id.chat_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.chat_swipetorefresh);
        tracker = new MessageTracker(getActivity());
        conversationModels = tracker.getAllConversations();
        setAdapter();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        conversationModels = tracker.getAllConversations();
                        setAdapter();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }, 1000);
            }
        });
    }

    private void setAdapter() {
        adapter = new ChatAdapter(conversationModels,getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

}
