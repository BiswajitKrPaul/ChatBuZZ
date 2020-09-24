package com.example.chattestapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private static final int PICK_IMAGE_ID = 234;
    public static String PAGE_TITLE = "Profile";
    public static String PROFILE_PIC_STORAGE = "profilepics";
    public static String PROFILE_THUMB_STORAGE = "thumbprofilepics";
    public static String TAG = "ProfileFragment";
    private static String USER_DB = "users";
    FloatingActionButton uploadPic;
    FirebaseUser mUser;
    StorageReference mStorage;
    DatabaseReference mDataBase;
    CircleImageView profilePic;
    Bitmap bitmap;
    Uri mCropImageUri;
    FloatingActionButton allUserFloatingActionButton;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public void getProfileImageUri(FirebaseUser user) {

        mDataBase.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getActivity()).load(user.getProfilepic()).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).placeholder(R.drawable.profile).into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        uploadPic = view.findViewById(R.id.profile_uploadImg);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference().child(PROFILE_PIC_STORAGE);
        mDataBase = FirebaseDatabase.getInstance().getReference().child(USER_DB);
        mDataBase.keepSynced(true);
        profilePic = view.findViewById(R.id.profile_imageview);
        getProfileImageUri(mUser);
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });
        return view;
    }


    private void UploadImage() {
        ImagePicker.Companion.with(this).cropSquare().compress(1024).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            UploadProfilePicToFireBase(data.getData());
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            ChatUtils.maketoast(getActivity(), "Error Uploading Image");
        } else {
            ChatUtils.maketoast(getActivity(), "Task Cancelled");
        }
    }

    private void UploadProfilePicToFireBase(final Uri data) {
        mStorage.child(mUser.getUid() + ".jpg").putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                try {
                    Bitmap bitmap = new Compressor(getContext()).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(new File(data.getPath()));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    final byte[] bytes = byteArrayOutputStream.toByteArray();
                    mStorage.child(PROFILE_THUMB_STORAGE).child(mUser.getUid() + ".jpg").putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mStorage.child(PROFILE_THUMB_STORAGE).child(mUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri1) {
                                    final HashMap map = new HashMap();
                                    map.put("thumbprofilepic", uri1.toString());
                                    mStorage.child(mUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri2) {
                                            map.put("profilepic", uri2.toString());
                                            mDataBase.child(mUser.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Glide.with(getActivity()).load(map.get("profilepic").toString()).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).placeholder(R.drawable.profile).into(profilePic);
                                                    ChatUtils.maketoast(getContext(), "Profile Pic Updated");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    ChatUtils.maketoast(getContext(), "Profile Pic Updated Failed : " + e.getMessage());
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ChatUtils.maketoast(getContext(), "Profile Pic Updated Failed : " + e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                ChatUtils.maketoast(getActivity(), "Error Occurred During Image Upload");
            }
        });
    }

}