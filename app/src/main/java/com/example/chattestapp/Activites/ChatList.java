package com.example.chattestapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.chattestapp.Adapters.MainViewPagerAdapter;
import com.example.chattestapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ChatList extends AppCompatActivity {

    static String USER_DB = "users";
    FirebaseAuth mAuth;
    ViewPager viewPager;
    MainViewPagerAdapter mainViewPagerAdapter;
    MaterialToolbar mToolBar;
    FirebaseUser firebaseUser;
    DatabaseReference mDataBase;
    TabLayout tabLayout;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mToolBar = findViewById(R.id.chatscreen_toolbar);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance().getReference().child(USER_DB);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(ChatList.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            viewPager = findViewById(R.id.chatlist_viewpager);
            tabLayout = findViewById(R.id.chatlist_tablayout);
            floatingActionButton = findViewById(R.id.chatlist_alluser);
            mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            viewPager.setAdapter(mainViewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() != 0) {
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            UpdateOnlineStatus("true");
            LoadMenuBar();
            RefreshToken();
        }
    }

    private void RefreshToken() {
    }

    private void UpdateOnlineStatus(String online) {

        if (mAuth.getCurrentUser() != null) {
            HashMap status = new HashMap();
            status.put("online", online);
            if ("false".equals(online))
                status.put("lastseen", ServerValue.TIMESTAMP);
            mDataBase.child(mAuth.getCurrentUser().getUid()).updateChildren(status);
        }
    }

    private void LoadMenuBar() {
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chatlist_logout:
                        if (mAuth.getCurrentUser() != null) {
                            HashMap status = new HashMap();
                            status.put("online", "false");
                            status.put("lastseen", ServerValue.TIMESTAMP);
                            mDataBase.child(mAuth.getCurrentUser().getUid()).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    mAuth.signOut();
                                    Intent intent = new Intent(ChatList.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void allUsers(View view) {
        Intent intent = new Intent(ChatList.this, AllUsers.class);
        startActivity(intent);
    }
}