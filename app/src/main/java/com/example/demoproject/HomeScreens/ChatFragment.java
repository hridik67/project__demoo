package com.example.demoproject.HomeScreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.Chats.ChatUserData;
import com.example.demoproject.Chats.SingleUserChatActivity;
import com.example.demoproject.Chats.UserListener;
import com.example.demoproject.HomePageActivity;
import com.example.demoproject.R;
import com.example.demoproject.UserDetails;
import com.example.demoproject.Chats.mychatuserAdapter;
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;

public class ChatFragment extends Fragment implements UserListener {

    RecyclerView recyclerView;
    AbstractList<ChatUserData> chatUserData;
    DatabaseReference matchList,reference;
    Context context;
    StorageReference storage;
    TextView nousertext2;
    HomePageActivity getactivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        context=this.getContext();
        nousertext2=view.findViewById(R.id.nousertext2);
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
        matchList = FirebaseDatabase.getInstance().getReference("Swipes");
        recyclerView=view.findViewById(R.id.recyclerView);
        getactivity =(HomePageActivity) getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatUserData=new ArrayList<>();
        getMatchlist();
        return view;
    }

    private void getMatchlist() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            matchList.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        //Toast.makeText(context, dataSnapshot.getValue().toString()+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        if (dataSnapshot.getValue().toString().equals("Matched")){
                            String user=dataSnapshot.getKey().toString();
                            //Toast.makeText(context, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            reference.child(dataSnapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.getResult().exists()){
                                        DataSnapshot snapshot1=task.getResult();
                                        UserDetails userDetails = snapshot1.getValue(UserDetails.class);
                                        //Toast.makeText(context, userDetails.getName().toString(), Toast.LENGTH_SHORT).show();
                                        getPhots(snapshot1.getKey().toString(),userDetails);
                                    }
                                    else {
                                        nousertext2.setVisibility(View.VISIBLE);
                                        Toast.makeText(context, "You have no Matches till yet Swipe and Find your Match", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void getPhots(String toString,UserDetails userDetails) {
        StorageReference reference=storage.child(toString+"Profile Picture"+1);
        try {
            File localfile= File.createTempFile(toString+"Profile Picture"+1,".jpg");
            reference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            ChatUserData chatUserData1=new ChatUserData(bitmap,userDetails.getName(),userDetails.getDescription(),toString);
                            chatUserData.add(chatUserData1);
                            recyclerView.setAdapter(new mychatuserAdapter(chatUserData,ChatFragment.this));
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
    }

    @Override
    public void onUserClicked(ChatUserData userData) {
        Intent intent = new Intent(getactivity, SingleUserChatActivity.class);
        intent.putExtra("otherusername",userData.getName());
        intent.putExtra("otheruserId",userData.getUserid());
        startActivity(intent);
    }
}