package com.example.chattestapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattestapp.Adapters.ChatListApdater;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatList extends AppCompatActivity {

    static String USER_DB = "users";
    static String TAG = "ChatList";
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    ChatListApdater chatListApdater;
    ArrayList<User> userList;
    DatabaseReference mDatabase;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mAuth = FirebaseAuth.getInstance();
        ChatUtils.maketoast(ChatList.this, "Welcome back : " + mAuth.getCurrentUser().getEmail());
        recyclerView = findViewById(R.id.chatlist_recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatList.this));
        LoadData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }

    private void LoadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER_DB);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<User>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(User.class);
                    if (!user.getUid().equalsIgnoreCase(mAuth.getUid()))
                        userList.add(user);
                }
                chatListApdater = new ChatListApdater(ChatList.this, userList);
                recyclerView.setAdapter(chatListApdater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.chatlist_logout:
                mAuth.signOut();
                Intent intent = new Intent(ChatList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}