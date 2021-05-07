package com.example.chattestapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.Listeners.CustomOnClickListener;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.example.chattestapp.ViewHolder.AllUserViewHolder;

import java.util.ArrayList;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserViewHolder> implements CustomOnClickListener {

    private final ArrayList<User> userList;
    private final Context context;

    public AllUserAdapter(ArrayList<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alluser_cardview, parent, false), AllUserAdapter.this);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllUserViewHolder holder, int position) {

        holder.username.setText(userList.get(position).getFirstname() + " " + userList.get(position).getLastname());

    }

    @Override
    public int getItemCount() {
        return userList.size() - 1;
    }

    @Override
    public void getPosition(int pos) {
        ChatUtils.maketoast(context, "Clicked On Button Pos : " + pos);
    }

    @Override
    public void getItemPostion(int pos) {
        //Not required to implement for now.
    }
}
