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
    private ArrayList<Chat> chatList = new ArrayList<Chat>();
    private String senderUid;

    public ChatScreenAdapter(Context context, String senderUid,ArrayList<Chat> chatList) {
        this.chatList=chatList;
        this.context = context;
        this.senderUid = senderUid;
    }

    /*public void updateMessageList(Chat chat) {
        chatList.add(chat);
        notifyItemInserted(chatList.size() - 1);
    }*/

    @NonNull
    @Override
    public ChatScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatScreenViewHolder(LayoutInflater.from(context).inflate(R.layout.chatscreen_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatScreenViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.recieverText.setVisibility(View.VISIBLE);
        holder.sendertext.setVisibility(View.VISIBLE);
        if (senderUid.equals(chat.getSenderUid())) {
            holder.sendertext.setText(chat.getMessageBody());
            holder.recieverText.setVisibility(View.GONE);
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
