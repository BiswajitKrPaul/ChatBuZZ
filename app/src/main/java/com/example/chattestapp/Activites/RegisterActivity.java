package com.example.chattestapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText et_first_name, et_middle_name, et_last_name, et_email, et_password, et_phoneno;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intialize();
        mAuth = FirebaseAuth.getInstance();
    }

    private void intialize() {
        et_first_name = findViewById(R.id.register_firstname);
        et_middle_name = findViewById(R.id.register_middlename);
        et_last_name = findViewById(R.id.register_lastname);
        et_email = findViewById(R.id.register_email);
        et_password = findViewById(R.id.register_password);
        et_phoneno = findViewById(R.id.register_phoneno);
    }

    public void RegisterDB(View view) {

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    ChatUtils.maketoast(getApplicationContext(), "Register Successful");

                    Intent intent = new Intent(RegisterActivity.this, ChatList.class);
                    startActivity(intent);
                } else {
                    ChatUtils.maketoast(getApplicationContext(), task.getException().getMessage().toString());
                }
            }
        });
    }
}