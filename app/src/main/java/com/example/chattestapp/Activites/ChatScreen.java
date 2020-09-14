package com.example.chattestapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chattestapp.Adapters.ChatScreenAdapter;
import com.example.chattestapp.DataBaseClasses.Chat;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatScreen extends AppCompatActivity {

    static String MESSAGE_DB = "chat";
    static String TAG = "ChatScreen";
    static int LIMIT_LAST_DEFAULT = 25;
    static int LIMIT_LAST_LOAD_MORE = 15;
    String recieverUid, senderUid, lastkey, prevlastkey, firstKey = "";
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText et_textBody;
    Boolean isSwipeAble = true, isScrolled = false;
    Chat chat;
    ArrayList<Chat> chats = new ArrayList<>();
    ChatScreenAdapter chatScreenAdapter;
    RecyclerView recyclerView;
    MaterialToolbar materialToolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    int itemPos = 0, itemPos1 = 0;
    private LinearLayoutManager linearLayoutManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        recieverUid = getIntent().getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        materialToolbar = findViewById(R.id.chatscreen_toolbar);
        senderUid = mAuth.getUid();
        et_textBody = findViewById(R.id.chatscreen_messagebody);
        chatScreenAdapter = new ChatScreenAdapter(ChatScreen.this, senderUid, chats);
        recyclerView = findViewById(R.id.chatscreen_recylerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManage = new LinearLayoutManager(ChatScreen.this);
        recyclerView.setLayoutManager(linearLayoutManage);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        chat = new Chat();
        /*isScrolled = false;
        isSwipeAble = true;*/
        recyclerView.setAdapter(chatScreenAdapter);
        chats.clear();
        LoadData();
        firstDataKey();
        OnClickToolBar();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isSwipeAble) {
                    LoadMoreData();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(chatScreenAdapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });
    }

    private void LoadMoreData() {
        itemPos = 0;
        if (chatScreenAdapter.getItemCount() > 0) {
            mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).orderByKey().endAt(lastkey).limitToLast(LIMIT_LAST_LOAD_MORE).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (!prevlastkey.equals(dataSnapshot.getKey())) {
                        chats.add(itemPos++, dataSnapshot.getValue(Chat.class));
                        chatScreenAdapter.notifyItemInserted(chats.indexOf(dataSnapshot.getValue(Chat.class)));
                    } else {
                        prevlastkey = lastkey;
                    }
                    if (itemPos == 1) {
                        lastkey = dataSnapshot.getKey();
                    }
                    if (firstKey.equals(lastkey)) {
                        isSwipeAble = false;
                    }
                    swipeRefreshLayout.setRefreshing(false);
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

    private void OnClickToolBar() {

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chatlist_logout:
                        mAuth.signOut();
                        Intent intent = new Intent(ChatScreen.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void LoadData() {
        chats.clear();
        chatScreenAdapter.notifyDataSetChanged();
        mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).limitToLast(LIMIT_LAST_DEFAULT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chats.add(dataSnapshot.getValue(Chat.class));
                itemPos1++;
                if (itemPos1 == 1) {
                    lastkey = dataSnapshot.getKey();
                    prevlastkey = dataSnapshot.getKey();
                }
                chatScreenAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chats.size() - 1);
                isSwipeAble = !firstKey.equals(lastkey);
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

    public void Send(View view) {
        String message = et_textBody.getText().toString();
        et_textBody.setText("");
        if (TextUtils.isEmpty(message)) {
            ChatUtils.maketoast(ChatScreen.this, "Please type message body");
        } else {
            chat.setSenderUid(senderUid);
            chat.setMessageBody(message);
            mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).push().setValue(chat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        mDatabase.child(MESSAGE_DB).child(recieverUid).child(senderUid).push().setValue(chat, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Log.i(TAG, "Completed posting text");
                                //ChatUtils.maketoast(ChatScreen.this, "Message Sent");
                            }
                        });
                    }
                }
            });
        }
    }

    public void firstDataKey() {
        mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).orderByKey().limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                firstKey = dataSnapshot.getKey();
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