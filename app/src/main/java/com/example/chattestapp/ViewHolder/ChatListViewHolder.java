package com.example.chattestapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.R;


public class ChatListViewHolder extends RecyclerView.ViewHolder {

    public TextView username,email;

    public ChatListViewHolder(@NonNull View itemView) {
        super(itemView);
        username=itemView.findViewById(R.id.chatlist_card_name);
        email=itemView.findViewById(R.id.chatlist_card_email);
    }
}
