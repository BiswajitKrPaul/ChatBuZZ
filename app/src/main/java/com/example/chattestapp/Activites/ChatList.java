package com.example.chattestapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.chattestapp.Adapters.ChatListApdater;
import com.example.chattestapp.Adapters.MainViewPagerAdapter;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatList extends AppCompatActivity {

    FirebaseAuth mAuth;
    ViewPager viewPager;
    MainViewPagerAdapter mainViewPagerAdapter;
    MaterialToolbar mToolBar;
    FirebaseUser firebaseUser;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mToolBar = findViewById(R.id.chatscreen_toolbar);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        if (firebaseUser == null) {
            Intent intent = new Intent(ChatList.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            ChatUtils.maketoast(ChatList.this, "Welcome back : " + mAuth.getCurrentUser().getEmail());
        }
        viewPager = findViewById(R.id.chatlist_viewpager);
        tabLayout = findViewById(R.id.chatlist_tablayout);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        LoadMenuBar();


    }

    private void LoadMenuBar() {
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chatlist_logout:
                        mAuth.signOut();
                        Intent intent = new Intent(ChatList.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

   /* @Override
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
    }*/

}