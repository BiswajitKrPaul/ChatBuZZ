package com.example.chattestapp.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chattestapp.Adapters.ChatScreenAdapter;
import com.example.chattestapp.DataBaseClasses.Chat;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.Notifications.APIService;
import com.example.chattestapp.Notifications.Client;
import com.example.chattestapp.Notifications.Data;
import com.example.chattestapp.Notifications.Response;
import com.example.chattestapp.Notifications.Sender;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatScreen extends AppCompatActivity {

    public static ValueEventListener seenListener;
    public static ChildEventListener seenListener1;
    static String MESSAGE_DB = "chat";
    static String USER_DB = "users";
    static String TAG = "ChatScreen";
    static int LIMIT_LAST_DEFAULT = 25;
    static int LIMIT_LAST_LOAD_MORE = 15;
    String recieverUid, senderUid, lastkey, prevlastkey, firstKey = "";
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText et_textBody;
    Boolean isSwipeAble = true;
    Chat chat;
    ArrayList<Chat> chats = new ArrayList<>();
    ChatScreenAdapter chatScreenAdapter;
    RecyclerView recyclerView;
    MaterialToolbar materialToolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    CircleImageView profilepic;
    int itemPos = 0, itemPos1 = 0;
    User currentUser;
    APIService apiService;
    Boolean notify = false;
    BitmapDrawable bitmap;
    Bitmap image;
    User senderUser;
    private LinearLayoutManager linearLayoutManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        recieverUid = getIntent().getStringExtra("uid");
        ChatUtils.Chat_UID = recieverUid;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        materialToolbar = findViewById(R.id.chatscreen_toolbar);
        senderUid = mAuth.getUid();
        currentUser = new User();
        senderUser = new User();
        et_textBody = findViewById(R.id.chatscreen_messagebody);
        recyclerView = findViewById(R.id.chatscreen_recylerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManage = new LinearLayoutManager(ChatScreen.this);
        profilepic = findViewById(R.id.chatscreen_profilepic);
        recyclerView.setLayoutManager(linearLayoutManage);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        chats.clear();
        firstDataKey();
        OnClickToolBar();
        LoadToolBar();
        SeenMessage();
        LoadData();
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

        seenListener = mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren() && chats.size() > 0) {
                    Chat chat = dataSnapshot.getChildren().iterator().next().getValue(Chat.class);
                    Chat tempChat = chats.get(chats.size() - 1);
                    if (chat.getChatid() != null) {
                        if (chat.isIsseen() != tempChat.isIsseen()) {
                            int pos = chats.indexOf(tempChat);
                            chats.remove(pos);
                            chats.add(pos, chat);
                            chatScreenAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        materialToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePictureView.class);
                intent.putExtra("userprofilepic", currentUser.getProfilepic());
                startActivity(intent);
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePictureView.class);
                intent.putExtra("userprofilepic", currentUser.getProfilepic());
                startActivity(intent);
            }
        });


    }

    private void LoadToolBar() {

        mDatabase.child(USER_DB).child(recieverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                materialToolbar.setTitle(currentUser.getFirstname() + " " + currentUser.getLastname());
                if ("true".equals(currentUser.getOnline())) {
                    materialToolbar.setSubtitle("Online");
                } else if ("false".equals(currentUser.getOnline())) {
                    materialToolbar.setSubtitle(getDate(currentUser.getLastseen()));
                }
                Glide.with(getApplicationContext()).load(currentUser.getThumbprofilepic()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile).into(profilepic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        chatScreenAdapter = new ChatScreenAdapter(getApplicationContext(), senderUid, chats, recieverUid);
        bitmap = (BitmapDrawable) profilepic.getDrawable();
        image = bitmap.getBitmap();
        recyclerView.setAdapter(chatScreenAdapter);
    }

    private String getDate(Long lastseen) {
        String lastseenDate = null;

        Timestamp timestamp = new Timestamp(lastseen);
        Date date = new Date(timestamp.getTime());

        SimpleDateFormat dateTimeDay = new SimpleDateFormat("EEE-dd hh:mm aa");
        SimpleDateFormat dateTime = new SimpleDateFormat("hh:mm aa");
        Date currentdate = new Date();
        if (date.before(currentdate))
            lastseenDate = "Last seen On " + dateTimeDay.format(date) + "";
        else
            lastseenDate = "Last seen Today On " + dateTime.format(date) + "";

        return lastseenDate;

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
                        if (mAuth.getCurrentUser() != null) {
                            HashMap status = new HashMap();
                            status.put("online", "false");
                            status.put("lastseen", ServerValue.TIMESTAMP);
                            mDatabase.child(USER_DB).child(mAuth.getCurrentUser().getUid()).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    mAuth.signOut();
                                    Intent intent = new Intent(ChatScreen.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
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
        final String message = et_textBody.getText().toString();
        et_textBody.setText("");
        if (TextUtils.isEmpty(message)) {
            ChatUtils.maketoast(ChatScreen.this, "Please type message body.....");
        } else {
            chat = new Chat();
            notify = true;
            chat.setSenderUid(senderUid);
            chat.setMessageBody(message);
            chat.setDelivertime(System.currentTimeMillis());
            mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).push().setValue(chat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("chatid", databaseReference.getKey());
                        hashMap.put("delivertime", ServerValue.TIMESTAMP);
                        mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).child(databaseReference.getKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                mDatabase.child(MESSAGE_DB).child(recieverUid).child(senderUid).push().setValue(chat, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("chatid", databaseReference.getKey());
                                            hashMap.put("delivertime", ServerValue.TIMESTAMP);
                                            mDatabase.child(MESSAGE_DB).child(recieverUid).child(senderUid).child(databaseReference.getKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Log.i(TAG, "Completed posting text");
                                                    if (notify) {
                                                        sendNotifiaction(recieverUid, currentUser.getFirstname(), chat.getMessageBody());
                                                    }
                                                    notify = false;
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    private void sendNotifiaction(final String recieverUid, final String firstname, final String message) {


        mDatabase.child(USER_DB).child(senderUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                senderUser = dataSnapshot.getValue(User.class);

                mDatabase.child(USER_DB).child(recieverUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String recieverToken = dataSnapshot.getValue(User.class).getToken();
                        Data data = new Data(senderUid, recieverUid, senderUser.getFirstname() + ": " + message, "New Message", R.drawable.app_image);
                        Sender sender = new Sender(data, recieverToken);
                        apiService.sendNotification(sender).enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                if (response.code() == 200) {
                                    if (response.body().success != 1) {
                                        ChatUtils.maketoast(ChatScreen.this, "Failed!");
                                    }
                                } else {
                                    //ChatUtils.maketoast(ChatScreen.this, "Notify");
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void firstDataKey() {
        mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).orderByKey().limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstKey = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UpdateOnlineStatus(String online) {
        if (mAuth.getCurrentUser() != null) {
            HashMap status = new HashMap();
            status.put("online", online);
            if ("false".equals(online))
                status.put("lastseen", ServerValue.TIMESTAMP);
            mDatabase.child(USER_DB).child(mAuth.getCurrentUser().getUid()).updateChildren(status);
        }
    }


    private void SeenMessage() {
        itemPos1 = 0;
        seenListener1 = mDatabase.child(MESSAGE_DB).child(recieverUid).child(senderUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Chat chat = dataSnapshot.getValue(Chat.class);
                if (!chat.getSenderUid().equals(senderUid)) {
                    HashMap hm = new HashMap();
                    hm.put("isseen", true);
                    hm.put("seentime", ServerValue.TIMESTAMP);
                    dataSnapshot.getRef().updateChildren(hm).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });
                }

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

    @Override
    protected void onPause() {
        super.onPause();
        if (seenListener1 != null)
            mDatabase.child(MESSAGE_DB).child(recieverUid).child(senderUid).removeEventListener(seenListener1);
        if (seenListener != null)
            mDatabase.child(MESSAGE_DB).child(senderUid).child(recieverUid).removeEventListener(seenListener);

        ChatUtils.Chat_UID = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemPos1 = 0;
        ChatUtils.Chat_UID = recieverUid;
    }
}