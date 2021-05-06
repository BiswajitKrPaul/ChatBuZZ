package com.example.chattestapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.Listeners.CustomOnClickListener;
import com.example.chattestapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserViewHolder extends RecyclerView.ViewHolder {

    public TextView username;
    public Button sendrequest;
    public CircleImageView profilePics;

    public AllUserViewHolder(@NonNull View itemView, final CustomOnClickListener clickListener) {
        super(itemView);
        username = itemView.findViewById(R.id.alluser_card_name);
        sendrequest = itemView.findViewById(R.id.alluser_sendrequest);
        profilePics = itemView.findViewById(R.id.alluser_profilePic);
        sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.getPosition(getAdapterPosition());
            }
        });
    }
}
