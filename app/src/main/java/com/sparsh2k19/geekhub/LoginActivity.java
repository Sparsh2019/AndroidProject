package com.sparsh2k19.geekhub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText name;
    LinearLayout signup;
    TextView warning;

    private boolean isSignup = false;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        signup = findViewById(R.id.signup);
        warning = findViewById(R.id.warning);
        final Button login = findViewById(R.id.login);
        final TextView toggle = findViewById(R.id.toggle);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isSignup) {
                    isSignup = false;
                    toggle.setText("Register");
                    signup.setVisibility(View.GONE);
                    login.setText("Login");
                }

                isSignup = true;
                toggle.setText("Already a user? Login");
                signup.setVisibility(View.VISIBLE);
                login.setText("Register");

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr = email.getText();
                final String passwordStr = password.getText();
                if(isSignup) {
                    final String nameStr = name.getText().toString();
                    warning.setVisibility(View.GONE);
                    AdditionalUserInfo info;
                    auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = task.getResult().getUser();
                                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameStr)
                                        .build();
                                user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent reply = new Intent();
                                            setResult(RESULT_OK, reply);
                                            finish();
                                        } else {
                                            warning.setVisibility(View.VISIBLE);
                                            warning.setText("Some error occured");
                                        }
                                    }
                                });
                            } else {
                                warning.setVisibility(View.VISIBLE);
                                warning.setText("EmailId is already being used");
                            }
                        }
                    });
                } else {
                    auth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent reply = new Intent();
                                setResult(RESULT_OK, reply);
                                finish();
                            } else {
                                warning.setVisibility(View.VISIBLE);
                                warning.setText("Some error occured");
                            }
                        }
                    });
                }
            }
        });
    }
}
