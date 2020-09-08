package com.example.chattestapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.DataBaseClasses.Chat;
import com.example.chattestapp.R;
import com.example.chattestapp.ViewHolder.ChatScreenViewHolder;

import java.util.ArrayList;

public class ChatScreenAdapter extends RecyclerView.Adapter<ChatScreenViewHolder> {

    Context context;
    private ArrayList<Chat> chatList;
    private String senderUid;

    public ChatScreenAdapter(Context context, ArrayList<Chat> chatList, String senderUid) {
        this.context = context;
        this.chatList = chatList;
        this.senderUid = senderUid;
    }

    @NonNull
    @Override
    public ChatScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatScreenViewHolder(LayoutInflater.from(context).inflate(R.layout.chatscreen_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatScreenViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        if (senderUid.equals(chat.getSenderUid())) {
            holder.sendertext.setText(chat.getMessageBody());
            holder.recieverText.setVisibility(View.INVISIBLE);
        } else {
            holder.sendertext.setVisibility(View.GONE);
            holder.recieverText.setText(chat.getMessageBody());
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
