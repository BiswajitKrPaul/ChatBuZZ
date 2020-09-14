package com.example.chattestapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private static final int PICK_IMAGE_ID = 234;
    public static String PAGE_TITLE = "Profile";
    public static String PROFILE_PIC_STORAGE = "profilepics";
    public static String TAG = "ProfileFragment";
    FloatingActionButton uploadPic;
    FirebaseUser mUser;
    StorageReference mStorage;
    CircleImageView profilePic;
    Bitmap bitmap;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public void getProfileImageUri(FirebaseUser user) {


        try {
            mStorage.child(user.getUid() + ".jpg").getBytes(2048 * 2048).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Glide.with(getActivity()).load(bitmap).placeholder(R.drawable.profile).into(profilePic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bitmap = null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        ImagePicker.Companion.with(this).cropSquare().compress(2048).start();

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
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println(data.getPath());
                Glide.with(getActivity()).load(data).placeholder(R.drawable.profile).diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic);
                ChatUtils.maketoast(getActivity(), "Image Changed");
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