package com.example.demoproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterationDetails3 extends AppCompatActivity {

    TextView DateOfBirth,male,female;
    Spinner other;
    EditText name;
    DatePickerDialog.OnDateSetListener setListener;
    static int age;
    MaterialCardView dob_view;
    ImageView profile_view,add_pic;
    static Uri imageUri;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    static int photo=0;
    StorageReference storage;
    MaterialButton Next;
    static String Name,dob,gender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gender="Others";
        setContentView(R.layout.activity_registeration_details3);
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        name=findViewById(R.id.namE);
        profile_view=findViewById(R.id.profilepic);
        add_pic=findViewById(R.id.add_pic);
        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
            }
        });
        DateOfBirth=findViewById(R.id.dob);
        dob_view=findViewById(R.id.dob_view);
        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        dob_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterationDetails3.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                DateOfBirth.setText(day +"/"+ month +"/"+ year);

            }
        };
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        other=findViewById(R.id.other);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="Male";
                male.setTextColor(Color.parseColor("#E15454"));
                male.setBackground(getDrawable(R.drawable.background_box_gender));
                //male.setBackgroundResource(R.drawable.background_box_gener1);
                female.setTextColor(getColor(com.google.android.material.R.color.m3_default_color_secondary_text));
                female.setBackgroundResource(R.drawable.background_box_gener1);
                other.setBackgroundResource(R.drawable.background_box_gener1);

            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender="Female";
                female.setTextColor(Color.parseColor("#E15454"));
                female.setBackground(getDrawable(R.drawable.background_box_gender));

                //female.setBackgroundResource(R.drawable.background_box_gener1);
                male.setTextColor(getColor(com.google.android.material.R.color.m3_default_color_secondary_text));
                male.setBackgroundResource(R.drawable.background_box_gener1);
                other.setBackgroundResource(R.drawable.background_box_gener1);

            }
        });
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RegisterationDetails3.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        other.setAdapter(myAdapter);
        other.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender=other.getSelectedItem().toString();
                male.setBackgroundResource(R.drawable.background_box_gener1);
                other.setBackgroundResource(R.drawable.background_box_gender);
                female.setBackgroundResource(R.drawable.background_box_gener1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Next =findViewById(R.id.NEXT3);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photo==1) {
                    if(name.getText().length()>0){
                        if (DateOfBirth.getText().toString().length() > 0) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String date = simpleDateFormat.format(Calendar.getInstance().getTime());
                            String yy = date.substring(date.length() - 4);
                            String d1 = DateOfBirth.getText().toString();
                            String yyuser = d1.substring(d1.length() - 4);
                            age = Integer.parseInt(yy) - Integer.parseInt(yyuser);
                            if (age >= 18) {
                                if(!(gender.equals("Others")) ) {
                                    dob = DateOfBirth.getText().toString();
                                    Name=name.getText().toString();
                                    Intent intent = new Intent(RegisterationDetails3.this, RegisterationDetails4.class);
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(RegisterationDetails3.this, "You must select a gender to move futher", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterationDetails3.this, "You must be above or equal to 18 to register", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterationDetails3.this, "Enter your Date of Birth to move further", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterationDetails3.this, "Enter your name to move further", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(RegisterationDetails3.this, "Provide a profile photo using camera icon to move further", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private String getFileExt(Uri selectedImageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(selectedImageUri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri selectedImageUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG" + timeStamp + "." + getFileExt(selectedImageUri);
                ImageView imageView = new ImageView(RegisterationDetails3.this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                uploadimage(selectedImageUri,imageView);
            }
        }
    }

    private void uploadimage(Uri imguri, ImageView imageView){
        String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference reference=storage.child(currentUid+"Profile Picture1");
        reference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                profile_view.setImageURI(imguri);
                                imageUri=imguri;
                                Toast.makeText(RegisterationDetails3.this, "Image succesfully uploaded", Toast.LENGTH_SHORT).show();
                                photo=1;

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterationDetails3.this, e.toString()+"Image not Uploaded,check your internet connection and try again!", Toast.LENGTH_LONG).show();
                        Log.e("info", "onFailure: "+e.toString());

                    }
                });
    }

}
