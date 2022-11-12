package com.example.demoproject.Chats;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.demoproject.FindMatchView.SliderAdapter;
import com.example.demoproject.Notification.SendNotification;
import com.example.demoproject.R;
import com.example.demoproject.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SingleUserChatActivity extends AppCompatActivity {

    TextView receiverUserName;
    EditText chatinput;
    ImageView sendMessage,vcbtn,backbuttonsinglechat;
    RecyclerView chatrecycleview;

    private ChatMessage chatMessage;
    final ArrayMap<Timestamp,ChatMessage> chatMessageList = new ArrayMap<>();
    final ArrayMap<Timestamp, Integer> chatMessageSenderOrUser = new ArrayMap<>();
    private SingleUserChatAdapter singleUserChatAdapter;
    ImageView reciever_photo;
    private static SimpleDateFormat sf;
    DatabaseReference chats;
    FirebaseUser currentUser1;
    String currentUser,otherUser,otherUserToken;
    StorageReference storage;
    UserDetails CurrentUser,OtherUser;
    LinearLayout linear;
    ConstraintLayout laayout1;
    ScrollView sc2;
    RelativeLayout rl2;

    SliderView sliderView;
    Bitmap[] images;
    static int flag=0;


    TextView swipe_profile_age2,swipe_profile_study,swipe_profile_work,swipe_profile_description,swipe_profile_zodiac_sign,swipe_profile_favourite_drink,swipe_profile_pets,swipe_profile_name2;
    ImageView backbuttonswipe_singlechatdetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser1 = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser1 != null;
        currentUser = currentUser1.getUid();
        otherUser=getIntent().getStringExtra("otheruserId");
        chats= FirebaseDatabase.getInstance().getReference("Chats");
        chatMessage=new ChatMessage();
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        setContentView(R.layout.activity_single_user_chat);
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        backbuttonsinglechat=findViewById(R.id.backbuttonsinglechat);
        backbuttonsinglechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        backbuttonswipe_singlechatdetails=findViewById(R.id.backbuttonswipe_singlechatdetails);
        backbuttonswipe_singlechatdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sc2=findViewById(R.id.sv2);
        rl2=findViewById(R.id.swipe2);
        laayout1=findViewById(R.id.layout_1);
        linear=findViewById(R.id.linear);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                laayout1.setVisibility(View.INVISIBLE);
                sc2.setVisibility(View.VISIBLE);
                rl2.setVisibility(View.VISIBLE);
            }
        });
        sliderView=findViewById(R.id.image_slider);
        swipe_profile_name2=findViewById(R.id.swipe_profile_name2);
        swipe_profile_age2=findViewById(R.id.swipe_profile_age2);
        swipe_profile_study=findViewById(R.id.swipe_profile_study);
        swipe_profile_work=findViewById(R.id.swipe_profile_work);
        swipe_profile_description=findViewById(R.id.swipe_profile_description);
        swipe_profile_zodiac_sign=findViewById(R.id.swipe_profile_zodiac_sign);
        swipe_profile_favourite_drink=findViewById(R.id.swipe_profile_favourite_drink);
        swipe_profile_pets=findViewById(R.id.swipe_profile_pets);


        reciever_photo=findViewById(R.id.reciever_photo);
        receiverUserName=findViewById(R.id.otheruserName);
        chatrecycleview=findViewById(R.id.chatrecycleview);
        vcbtn=findViewById(R.id.vcbtn);
        sendMessage=findViewById(R.id.send_message);
        chatinput=findViewById(R.id.chatinput);
        vcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SingleUserChatActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SingleUserChatActivity.this, VideoCallOutgoing.class);
                intent.putExtra("uid",otherUser);
                intent.putExtra("chattoken",CurrentUser.getChattoken());
                intent.putExtra("sendername",CurrentUser.getName());
                intent.putExtra("recievername",OtherUser.getName());
                startActivity(intent);
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatinput.getText().length()>0) {
                    chatMessage.setMessage(chatinput.getText().toString());
                    Date date = new Date();
                    String time=sf.format(new Timestamp(date.getTime())).toString();
                    chatMessage.setDateTime(time);
                    //Toast.makeText(SingleUserChatActivity.this, chatMessage.message + " time- " + chatMessage.dateTime, Toast.LENGTH_SHORT).show();
                    chats.child(currentUser).child(otherUser).child(chatMessage.dateTime).setValue(chatMessage);
                    SendNotification.Companion.send(CurrentUser.getName(),CurrentUser.getChattoken(),chatinput.getText().toString(),currentUser);
                    //sendNotification.send(CurrentUser.getName(),CurrentUser.getChattoken(),chatinput.getText().toString());
                    //sendNotification(CurrentUser,OtherUser);

                    chatinput.getText().clear();

                }else {
                    //Toast.makeText(SingleUserChatActivity.this, "Enter the Message first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        init();
        chatusrsDetails();
        //loadRecceiverDetails();
    }

    @Override
    public void onBackPressed() {
        if (flag==1){
            flag=0;
            laayout1.setVisibility(View.VISIBLE);
            sc2.setVisibility(View.INVISIBLE);
            rl2.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    private void chatusrsDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
        reference.child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                CurrentUser=task.getResult().getValue(UserDetails.class);
            }
        });
        reference.child(otherUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                OtherUser=task.getResult().getValue(UserDetails.class);
                loadRecceiverDetails();
            }
        });
    }

    //private void sendNotification(PushNotification notification) = CoroutineScope(Dispatchers.IO

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        singleUserChatAdapter=new SingleUserChatAdapter(chatMessageList,chatMessageSenderOrUser);
        chatrecycleview.setAdapter(singleUserChatAdapter);
    }
    private void loadRecceiverDetails() {
        String name=getIntent().getStringExtra("otherusername");
        receiverUserName.setText(name);
        swipe_profile_name2.setText(OtherUser.getName());
        swipe_profile_age2.setText(String.valueOf(OtherUser.getAge()));
        swipe_profile_study.setText("Study at " + OtherUser.getStudy());
        swipe_profile_work.setText("Works at " + OtherUser.getWork());
        swipe_profile_description.setText(OtherUser.getDescription());
        swipe_profile_zodiac_sign.setText(OtherUser.getZodiac());
        swipe_profile_favourite_drink.setText(OtherUser.getDrinking());
        swipe_profile_pets.setText(OtherUser.getPets());
        images=new Bitmap[OtherUser.getNoOfImage()];
        for (int i = 0; i < OtherUser.getNoOfImage(); i++) {
            StorageReference reference = storage.child(otherUser + "Profile Picture" + (i + 1));
            try {
                File localfile = File.createTempFile(otherUser + "Profile Picture" + (i + 1), ".jpg");
                int finalI = i;
                reference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    if (finalI == 0) {
                                        reciever_photo.setImageBitmap(bitmap);
                                    }
                                    images[finalI] = bitmap;

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
            }
            catch (IOException e) {
                    e.printStackTrace();
            }
        }
        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        backendMessage();

    }

    private void backendMessage() {
        chats.child(currentUser).child(otherUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                    assert message != null;
                    Timestamp time= Timestamp.valueOf(message.getDateTime());
                    chatMessageList.put(time,message);
                    chatMessageSenderOrUser.put(time,1);
                    singleUserChatAdapter.notifyItemInserted(chatMessageList.size());
                    chatrecycleview.smoothScrollToPosition(chatMessageList.size());
                }
                //singleUserChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        chats.child(otherUser).child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                    assert message != null;
                    Timestamp time= Timestamp.valueOf(message.getDateTime());
                    chatMessageList.put(time,message);
                    chatMessageSenderOrUser.put(time,2);
                    singleUserChatAdapter.notifyItemInserted(chatMessageList.size());
                    chatrecycleview.smoothScrollToPosition(chatMessageList.size());
                }
                //singleUserChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*chats.child(currentUser).child(otherUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot= task.getResult();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                    Timestamp time= Timestamp.valueOf(message.getDateTime());
                    chatMessageList.put(time,message);
                    chatMessageSenderOrUser.put(time,1);
                }
            }
        });
        chats.child(otherUser).child(currentUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot= task.getResult();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                    Timestamp time= Timestamp.valueOf(message.getDateTime());
                    chatMessageList.put(time,message);
                    chatMessageSenderOrUser.put(time,2);
                }
                showmessages();
            }
        });

         */
    }

    private void showmessages() {
        singleUserChatAdapter=new SingleUserChatAdapter(chatMessageList,chatMessageSenderOrUser);
        chatrecycleview.setAdapter(singleUserChatAdapter);
        for (int i=0;i<chatMessageList.size();i++){
            //Toast.makeText(SingleUserChatActivity.this, chatMessageList.valueAt(i).message+chatMessageSenderOrUser.valueAt(i), Toast.LENGTH_SHORT).show();
            Log.e("checkit"+chatMessageList.valueAt(i).dateTime,chatMessageList.valueAt(i).message);
        }
    }

}