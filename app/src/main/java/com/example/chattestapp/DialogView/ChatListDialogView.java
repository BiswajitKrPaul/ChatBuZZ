package com.example.chattestapp.DialogView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chattestapp.Activites.ChatScreen;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;

import java.util.Objects;

public class ChatListDialogView extends Dialog {

    ImageView vProfilePic;
    Context context;
    User user;
    ImageButton vDialogChat;

    public ChatListDialogView(@NonNull Context context, User user) {
        super(context);
        this.context = context;
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profilepic_dialogview);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(getWindow().getAttributes()));
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        vProfilePic = findViewById(R.id.dialog_profilepic);
        vDialogChat = findViewById(R.id.dialog_chat);
        Glide.with(context).load(user.getProfilepic())
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile)
                .into(vProfilePic);
        vDialogChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatScreen.class);
                intent.putExtra("uid", user.getUid());
                context.startActivity(intent);
                dismiss();
            }
        });

    }
}
