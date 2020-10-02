package com.example.chattestapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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

    private ArrayList<User> userList;
    private Context context;

    public AllUserAdapter(ArrayList<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alluser_cardview, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserViewHolder holder, int position) {

        holder.username.setText(userList.get(position).getFirstname() + " " + userList.get(position).getLastname());
        holder.sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatUtils.maketoast(context, userList.get(position).getFirstname());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size() - 1;
    }

    @Override
    public void getPosition(int pos) {
        ChatUtils.maketoast(context, "Clicked On Pos : " + pos);
    }
}
