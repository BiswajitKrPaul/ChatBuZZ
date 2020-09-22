package com.example.chattestapp.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chattestapp.DataBaseClasses.User;
import com.example.chattestapp.R;
import com.example.chattestapp.Utils.ChatUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class RegisterActivity extends AppCompatActivity {

    private static String USER_DB = "users";
    private static String TAG = "RegisterActivity";
    EditText et_first_name, et_middle_name, et_last_name, et_email, et_password, et_phone;
    String firstname, middlename, lastname, email, phone, password;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intialize();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void intialize() {
        et_first_name = findViewById(R.id.register_firstname);
        et_middle_name = findViewById(R.id.register_middlename);
        et_last_name = findViewById(R.id.register_lastname);
        et_email = findViewById(R.id.register_email);
        et_password = findViewById(R.id.register_password);
        et_phone = findViewById(R.id.register_phoneno);
    }

    private void fetchFields() {
        firstname = et_first_name.getText().toString();
        middlename = et_middle_name.getText().toString();
        if (TextUtils.isEmpty(middlename)) {
            middlename = "";
        }
        lastname = et_last_name.getText().toString();
        phone = et_phone.getText().toString();
        password = et_password.getText().toString();
        email = et_email.getText().toString();
    }

    private boolean checkFields() {
        fetchFields();
        if (TextUtils.isEmpty(firstname)) {
            et_first_name.setError("First Name can't be blank");
            return false;
        } else if (TextUtils.isEmpty(lastname)) {
            et_last_name.setError("Last Name can't be blank");
            return false;
        } else if (TextUtils.isEmpty(email)) {
            et_email.setError("Email can't be blank");
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            et_phone.setError("Phone number can't be blank");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            et_password.setError("Password is blank");
            return false;
        }
        return true;
    }

    public void RegisterDB(View view) {

        if (checkFields()) {
            final User user = new User();
            user.setEmail(email);
            user.setFirstname(firstname);
            user.setMiddlename(middlename);
            user.setLastname(lastname);
            user.setPhoneno(phone);
            user.setOnline("true");

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        try {
                            user.setUid(mAuth.getUid());
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (task.isSuccessful()) {
                                        user.setToken(task.getResult().getToken());
                                        mDatabase.child(USER_DB).child(mAuth.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    Intent intent = new Intent(RegisterActivity.this, ChatList.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                } else {
                                                    mAuth.getCurrentUser().delete();
                                                    Log.e(TAG, databaseError.getMessage());
                                                    ChatUtils.maketoast(getApplicationContext(), "Registration Failed due to : " + databaseError.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } catch (Exception e) {
                            mAuth.getCurrentUser().delete();
                            Log.e(TAG, e.getMessage());
                            ChatUtils.maketoast(getApplicationContext(), "Registration Failed due to : " + e.getMessage());
                        }
                    } else {
                        Log.e(TAG, task.getException().getMessage());
                        ChatUtils.maketoast(getApplicationContext(), "Registration Failed due to : " + task.getException().getMessage());
                    }
                }
            });
        }
    }
}
