package com.example.chattestapp.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chattestapp.R;

public class ProfilePictureView extends AppCompatActivity {

    ImageView imageView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileimage_view);
        imageView = findViewById(R.id.profilepictureview_imageView);
        imageButton = findViewById(R.id.profilepictureview_back);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        String userprofilepic = getIntent().getStringExtra("userprofilepic");
        Glide.with(getApplicationContext()).load(userprofilepic)
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.profile)
                .into(imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}