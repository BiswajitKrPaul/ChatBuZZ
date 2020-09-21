package com.example.chattestapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatScreenViewHolder extends RecyclerView.ViewHolder {

    public TextView sendertext, recieverText, seenTxt;
    public CircleImageView recieverProfilePic;

    public ChatScreenViewHolder(@NonNull View itemView) {
        super(itemView);
        sendertext = itemView.findViewById(R.id.sender_messsage_text);
        recieverText = itemView.findViewById(R.id.receiver_message_text);
        seenTxt = itemView.findViewById(R.id.sender_message_seen);
        recieverProfilePic = itemView.findViewById(R.id.chatscreen_recieverdp);
    }
}
