package com.example.chattestapp.Activites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.Adapters.AllUserAdapter;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AllUsers extends AppCompatActivity {

    private static String USER_DB = "users";
    private RecyclerView recyclerView;
    private ArrayList<User> userList;
    private LinearLayoutManager linearLayoutManager;
    private AllUserAdapter allUserAdapter;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        recyclerView = findViewById(R.id.alluser_recylerview);
        linearLayoutManager = new LinearLayoutManager(AllUsers.this);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setHasFixedSize(true);
        userList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DB);
        mDatabase.keepSynced(true);
        allUserAdapter = new AllUserAdapter(userList, AllUsers.this);
        recyclerView.setAdapter(allUserAdapter);
        LoadData();
    }

    private void LoadData() {
        userList.clear();
        allUserAdapter.notifyDataSetChanged();
        mDatabase.orderByChild("firstname").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!firebaseUser.getUid().equals(dataSnapshot.getValue(User.class).getUid())) {
                    userList.add(dataSnapshot.getValue(User.class));
                }
                allUserAdapter.notifyDataSetChanged();
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

    }
}