package com.example.chattestapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText et_login_email, et_login_password;
    String email, password;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, ChatList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void fetchFields() {
        email = et_login_email.getText().toString();
        password = et_login_password.getText().toString();
    }

    private void initialize() {
        et_login_email = findViewById(R.id.Login_Email);
        et_login_password = findViewById(R.id.Login_Password);
    }

    private boolean checkFields() {
        if (TextUtils.isEmpty(email)) {
            et_login_email.setError("Email is blank");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            et_login_password.setError("Password is blank");
            return false;
        }
        return true;
    }

    public void login(View view) {

        fetchFields();

        if (checkFields()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, ChatList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        ChatUtils.maketoast(MainActivity.this, "Login Failed due to : " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    public void Register(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}