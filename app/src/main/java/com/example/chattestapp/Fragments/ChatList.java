package com.example.chattestapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.Adapters.ChatListApdater;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatList extends Fragment {


    public static String PAGE_TITLE = "Chats";
    static String USER_DB = "users";
    static String TAG = "ChatList";
    RecyclerView recyclerView;
    ChatListApdater chatListApdater;
    ArrayList<User> userList;
    DatabaseReference mDatabase;
    User user;
    FirebaseAuth mAuth;
    private View viewRoot;


    public ChatList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatList newInstance() {
        return new ChatList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = viewRoot.findViewById(R.id.chatlist_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LoadData();
        return viewRoot;

    }

    private void LoadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DB);
        mDatabase.orderByChild("firstname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<User>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(User.class);
                    if (!user.getUid().equalsIgnoreCase(mAuth.getUid()))
                        userList.add(user);

                }
                chatListApdater = new ChatListApdater(getActivity(), userList);
                recyclerView.setAdapter(chatListApdater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }
}