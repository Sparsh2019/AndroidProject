package com.sparsh2k19.geekhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import static com.sparsh2k19.geekhub.Constants.CAMERA_REQUEST;
import static com.sparsh2k19.geekhub.Constants.CODE_REQUEST;
import static com.sparsh2k19.geekhub.Constants.LOGIN_REQUEST;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference messagesData;
    StorageReference storageReference;

    ListView messages;
    ArrayList<Message> messageList = new ArrayList<>();
    MessageAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages = findViewById(R.id.messages);
        adapter = new MessageAdapter(this, messageList);
        messages.setAdapter(adapter);

        FirebaseApp.initializeApp(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        messagesData = FirebaseDatabase.getInstance().getReference().child("general");
        messagesData.addChildEventListener(messageListner);

        ImageButton camera = findViewById(R.id.capture);
        ImageButton code = findViewById(R.id.code);
        ImageButton send = findViewById(R.id.send);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent codeIntent = new Intent(MainActivity.this, CodeListEvent.class);
                startActivityForResult(codeIntent, CODE_REQUEST);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageView = findViewById(R.id.message_edit);
                String messageStr = messageView.getText().toString();
                messageView.setText("");
                Message message = new Message(user.getDisplayName(), messageStr, new Date().getTime(), user.getUid());
                messagesData.push().setValue(message);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, LOGIN_REQUEST);
        }
        authStateListener.onAuthStateChanged(auth);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case LOGIN_REQUEST:
                user = auth.getCurrentUser();

            case CAMERA_REQUEST:
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                final Message imageMessage = new Message(user.getDisplayName(), "", new Date().getTime(), user.getUid());;
                storageReference.child("Images").child(user.getUid()).child(String.valueOf(imageMessage.getTime()).concat(".png")).putBytes(bytes)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUri = taskSnapshot.getDownloadUrl();
                                imageMessage.setImageUri(downloadUri.toString());
                                messagesData.push().setValue(imageMessage);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            case CODE_REQUEST:
                final Message codeMessage = new Message(user.getDisplayName(), "", new Date().getTime(), user.getUid());
                FirebaseDatabase.getInstance().getReference().child("code").child(data.getStringExtra("refId")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Code code = dataSnapshot.getValue(Code.class);
                        codeMessage.setCode(code);
                        messagesData.push().setValue(codeMessage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();
        }
    };

    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.codepg:
                    Intent codepgIntent = new Intent(MainActivity.this, CodePlaygroundActivity.class);
                    startActivity(codepgIntent);
                    return true;
                case R.id.signout:
                    auth.signOut();
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(login, LOGIN_REQUEST);
                    return true;
            } return false;
        }
    };

    ChildEventListener messageListner = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            final Message message = dataSnapshot.getValue(Message.class);
            messageList.add(message);
            adapter.notifyDataSetChanged();
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
    };
}
