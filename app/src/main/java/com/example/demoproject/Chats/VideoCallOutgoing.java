package com.example.demoproject.Chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.Notification.SendNotification;
import com.example.demoproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VideoCallOutgoing extends AppCompatActivity {

    ImageView imageView;
    TextView tvname;
    FloatingActionButton declinebtn;
    String reciever_name,reciever_token,reciever_uid,sender_name,sender_uid;
    StorageReference storage;
    DatabaseReference reference_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_outgoing);
        sender_uid=FirebaseAuth.getInstance().getUid();
        reference_response = FirebaseDatabase.getInstance().getReference("CallResponse");
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        imageView = findViewById(R.id.iv_og_vc);
        tvname = findViewById(R.id.name_vc_og);
        declinebtn = findViewById(R.id.decline_vc_og);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            reciever_uid = bundle.getString("uid");
            reciever_name = bundle.getString("recievername");
            sender_name = bundle.getString("sendername");
            reciever_token = bundle.getString("chattoken");

        } else{
            //Toast.makeText(this, "Data Missing", Toast.LENGTH_SHORT).show();
        }
        tvname.setText(reciever_name);
        StorageReference reference=storage.child(reciever_uid+"Profile Picture"+1);
        try {
            File localfile= File.createTempFile(reciever_uid+"Profile Picture"+1,".jpg");
            reference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendCallInvitation();
        checkResponse();
    }

    private void checkResponse() {
        reference_response.child(sender_uid).child(reciever_uid).child("res").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String key = snapshot.child("key").getValue().toString();
                    String response = snapshot.child("response").getValue().toString();
                    if (response.equals("yes")){
                        joinmeeting(key);
                        //Toast.makeText(VideoCallOutgoing.this, "Call Accepted", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("no")){
                        //Toast.makeText(VideoCallOutgoing.this, "Call denied", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VideoCallOutgoing.this,SingleUserChatActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else{
                    //Toast.makeText(VideoCallOutgoing.this, "Not Responding", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void joinmeeting(String key) {
        try {
            JitsiMeetConferenceOptions options= new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(key)
                    //.setFeatureFlag("PendingIntent", PendingIntent.FLAG_IMMUTABLE)
                    .setWelcomePageEnabled(false)

                    .build();
            //PendingIntent.get
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, alarmID, JitsiMeetOngoingConferenceService, PendingIntent.FLAG_IMMUTABLE);
            JitsiMeetActivity.launch(VideoCallOutgoing.this,options);
            finish();

        } catch (Exception e){
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void sendCallInvitation() {
        SendNotification sendNotification = new SendNotification();
        SendNotification.Companion.send(sender_name,reciever_token,"Is Calling you answer it",sender_uid);
    }
}