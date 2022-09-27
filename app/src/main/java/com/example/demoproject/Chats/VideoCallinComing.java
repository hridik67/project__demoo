package com.example.demoproject.Chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetOngoingConferenceService;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VideoCallinComing extends AppCompatActivity {
    String sender_uid,sender_name,reciever_uid;
    TextView sender;
    ImageView imageView;
    DatabaseReference reference_vc;
    FloatingActionButton decline,accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_callin_coming);
        sender=findViewById(R.id.name_vc_ic);
        imageView=findViewById(R.id.iv_ic_vc);
        decline=findViewById(R.id.decline_vc_ic);
        accept=findViewById(R.id.accept_vc_ic);
        sender_uid = getIntent().getStringExtra("sender_uid");
        sender_name = getIntent().getStringExtra("sender_name");
        sender.setText(sender_name);
        reciever_uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        StorageReference reference=storage.child(sender_uid+"Profile Picture"+1);
        try {
            File localfile= File.createTempFile(sender_uid+"Profile Picture"+1,".jpg");
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
        reference_vc = FirebaseDatabase.getInstance().getReference("CallResponse").child(sender_uid).child(reciever_uid);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = "yes";
                sendResponse(response);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = "no";
                sendResponse(response);
                Intent intent = new Intent(VideoCallinComing.this,SingleUserChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Toast.makeText(this, "sender uid is - "+sender_uid, Toast.LENGTH_SHORT).show();
    }

    private void sendResponse(String response) {
        if (response.equals("yes")){
            reference_vc.child("res").child("key").setValue(sender_name+reciever_uid);
            reference_vc.child("res").child("response").setValue(response);
            joinmeeting();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reference_vc.removeValue();
                }
            },3000);

        } else if (response.equals("no")){

            reference_vc.child("res").child("key").setValue(sender_name+reciever_uid);
            reference_vc.child("res").child("response").setValue(response);
            joinmeeting();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reference_vc.removeValue();
                }
            },3000);
        }
    }

    private void joinmeeting() {
        try {
            JitsiMeetConferenceOptions options= new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))

                    .setRoom(sender_name+reciever_uid)

                    .setWelcomePageEnabled(false)

                    .build();

            JitsiMeetActivity.launch(VideoCallinComing.this,options);
            finish();

        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}