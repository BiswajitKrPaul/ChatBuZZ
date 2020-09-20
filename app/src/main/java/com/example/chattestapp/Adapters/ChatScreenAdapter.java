package com.example.chattestapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chattestapp.DataBaseClasses.Chat;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.example.chattestapp.ViewHolder.ChatScreenViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatScreenAdapter extends RecyclerView.Adapter<ChatScreenViewHolder> {

    Context context;
    private ArrayList<Chat> chatList = new ArrayList<Chat>();
    private String senderUid;
    private String recieverUid;
    private DatabaseReference mDatabase;

    public ChatScreenAdapter(Context context, String senderUid, ArrayList<Chat> chatList, String recieverUid) {
        this.chatList = chatList;
        this.context = context;
        this.senderUid = senderUid;
        this.recieverUid = recieverUid;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
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
    public void onBindViewHolder(@NonNull final ChatScreenViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.recieverText.setVisibility(View.VISIBLE);
        holder.sendertext.setVisibility(View.VISIBLE);
        holder.recieverProfilePic.setVisibility(View.VISIBLE);
        if (senderUid.equals(chat.getSenderUid())) {
            holder.sendertext.setText(chat.getMessageBody());
            holder.recieverText.setVisibility(View.GONE);
            holder.recieverProfilePic.setVisibility(View.GONE);
        } else {
            holder.sendertext.setVisibility(View.GONE);
            holder.recieverText.setText(chat.getMessageBody());
            mDatabase.child(recieverUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Glide.with(context).load(dataSnapshot.getValue(User.class).getThumbprofilepic()).placeholder(R.drawable.profile).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.recieverProfilePic);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
