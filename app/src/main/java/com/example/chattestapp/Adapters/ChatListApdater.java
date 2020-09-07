package com.example.chattestapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.Activites.ChatScreen;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.Listeners.CustomOnClickListener;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.example.chattestapp.ViewHolder.ChatListViewHolder;

import java.util.ArrayList;

public class ChatListApdater extends RecyclerView.Adapter<ChatListViewHolder> implements CustomOnClickListener {

    private Context context;
    private ArrayList<User> userlist;


    public ChatListApdater(Context context, ArrayList<User> userlist) {
        this.context = context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(context).inflate(R.layout.chatlist_cardview, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.username.setText(userlist.get(position).getFirstname() + " " + userlist.get(position).getLastname());
        holder.email.setText(userlist.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public void getPosition(int pos) {
        //ChatUtils.maketoast(context, userlist.get(pos).getFirstname());
        Intent intent=new Intent(context, ChatScreen.class);
        intent.putExtra("uid",userlist.get(pos).getUid());
        context.startActivity(intent);
    }
}
