package com.example.chattestapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chattestapp.Activites.ChatScreen;
import com.example.chattestapp.DataBaseClasses.Chat;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.Listeners.CustomOnClickListener;
import com.example.chattestapp.R;
import com.example.chattestapp.ViewHolder.ChatListViewHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatListApdater extends RecyclerView.Adapter<ChatListViewHolder> implements CustomOnClickListener {

    public static String PROFILE_PIC_STORAGE = "profilepics";
    public static String PROFILE_THUMB_STORAGE = "thumbprofilepics";
    static String MESSAGE_DB = "chat";
    DatabaseReference mDatabase;
    StorageReference mStorage;
    String lastText = "";
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
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, int position) {
        holder.username.setText(userlist.get(position).getFirstname() + " " + userlist.get(position).getLastname());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference().child(PROFILE_PIC_STORAGE).child(PROFILE_THUMB_STORAGE);
        mDatabase.child(MESSAGE_DB).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userlist.get(position).getUid()).orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                lastText = dataSnapshot.getValue(Chat.class).getMessageBody();
                if ("".equals(lastText))
                    holder.email.setText("");
                else
                    holder.email.setText(lastText);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStorage.child(userlist.get(position).getUid() + ".jpg").getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Glide.with(context).load(bitmap).placeholder(R.drawable.profile).into(holder.profilePics);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public void getPosition(int pos) {
        //ChatUtils.maketoast(context, userlist.get(pos).getFirstname());
        Intent intent = new Intent(context, ChatScreen.class);
        intent.putExtra("uid", userlist.get(pos).getUid());
        context.startActivity(intent);
    }
}
