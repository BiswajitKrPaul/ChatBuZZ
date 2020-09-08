package com.example.chattestapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.R;


public class ChatScreenViewHolder extends RecyclerView.ViewHolder{

    public TextView sendertext,recieverText;
    public ChatScreenViewHolder(@NonNull View itemView) {
        super(itemView);
        sendertext=itemView.findViewById(R.id.sender_messsage_text);
        recieverText=itemView.findViewById(R.id.receiver_message_text);
    }
}
