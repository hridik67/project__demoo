package com.example.demoproject.HomeScreens;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.Chats.SingleUserChatActivity;
import com.example.demoproject.HomePageActivity;
import com.example.demoproject.R;
import com.example.demoproject.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileFragment extends Fragment {

    UserDetails userDetails;
    FirebaseUser currentUser;
    DatabaseReference reference;
    LinearLayout addingPhotosofUsers;
    EditText UserName,UserDescription,UserEmail;
    TextView UserDOB;
    DatePickerDialog.OnDateSetListener setListener;
    static int age,code;
    ImageView photoadd;
    public static int flag;
    Spinner UserGender;
    Context context;
    HomePageActivity homePageActivity;
    StorageReference storage;
    static ArrayList<ImageView> imagery1;
    static ArrayList<Uri> imageUri;
    String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        addingPhotosofUsers=view.findViewById(R.id.addingPhotosofUsers);
        UserName=view.findViewById(R.id.UserName);
        context=getActivity();
        flag=0;
        imagery1=new ArrayList<>();
        imageUri=new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
        photoadd=view.findViewById(R.id.photo1);
        homePageActivity=(HomePageActivity) getActivity();
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        UserGender=view.findViewById(R.id.UserGender);
        UserDescription=view.findViewById(R.id.UserDescription);
        UserEmail=view.findViewById(R.id.UserEmail);
        UserDOB=view.findViewById(R.id.UserDOB);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(context, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UserGender.setAdapter(myAdapter);
        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        photoadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked man", Toast.LENGTH_SHORT).show();
                code=0;
                changephoto(0);
            }
        });
        UserDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                UserDOB.setText(day +"/"+ month +"/"+ year);

            }
        };
        reference.child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userDetails=task.getResult().getValue(UserDetails.class);
                UserName.setText(userDetails.getName());
                int position=myAdapter.getPosition(userDetails.getGender());
                UserGender.setSelection(position);
                //UserGender.setText(userDetails.getGender());
                UserDescription.setText(userDetails.getDescription());
                UserEmail.setText(userDetails.getEmail());
                UserDOB.setText(userDetails.getDob());
                getImages(userDetails.getNoOfImage());
            }
        });



        MaterialButton materialButton=view.findViewById(R.id.submit);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(Calendar.getInstance().getTime());
                String yy = date.substring(date.length() - 4);
                String d1= UserDOB.getText().toString();
                String yyuser= d1.substring(d1.length()-4);
                age= Integer.parseInt(yy) - Integer.parseInt(yyuser);
                if (!(UserGender.getSelectedItem().toString().equals("Select"))){
                    if (age >= 18) {
                        DatabaseReference updatereference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
                        flag = 1;
                        userDetails.setName(UserName.getText().toString());
                        userDetails.setDob(UserDOB.getText().toString());
                        userDetails.setEmail(UserEmail.getText().toString());
                        userDetails.setDescription(UserDescription.getText().toString());
                        userDetails.setGender(UserGender.getSelectedItem().toString());
                        updatereference.child(currentUser.getUid()).setValue(userDetails);
                        Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
                        //intent.putExtra("otheruserId",userData.getUserid());
                        //          updatereference.child(currentUser.getUid()).child("gender").setValue(UserGender.getSelectedItem().toString());
                        //        updatereference.child(currentUser.getUid()).child("description").setValue(UserDescription.getText().toString());
                        //      updatereference.child(currentUser.getUid()).child("email").setValue(UserEmail.getText().toString());
                        //    updatereference.child(currentUser.getUid()).child("dob").setValue(UserDOB.getText().toString());
                        //  updatereference.child(currentUser.getUid()).child("noOfImage").setValue(imagery1.size());

                    } else {
                        Toast.makeText(context, "You must be above or equal to 18 to register", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "You must be select a gender to update your profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void updatephoto(Uri imguri,int noOfImage) {
        StorageReference reference=storage.child(currentUser.getUid()+"Profile Picture"+(noOfImage+1));
        reference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Toast.makeText(context, "Image succesfully uploaded", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.toString()+"Image not Uploaded,check your internet connection and try again!", Toast.LENGTH_LONG).show();
                        Log.e("info", "onFailure: "+e.toString());

                    }
                });

    }

    private void getImages(int noOfImage) {
        for (int i=0;i<noOfImage;i++){
            StorageReference reference=storage.child(currentUser.getUid()+"Profile Picture"+(i+1));
            try {
                File localfile= File.createTempFile(currentUser.getUid()+"Profile Picture"+i,".jpg");
                int finalI = i;
                reference.getFile(localfile)
                        .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {

                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                ImageView imageView = new ImageView(context);
                                imageView.setImageResource(R.mipmap.ic_launcher);
                                imageView.setImageBitmap(bitmap);
                                addvieW1(imageView,bitmap, finalI +1);

                            }
                        })
                        /*.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                ImageView imageView = new ImageView(context);
                                imageView.setImageResource(R.mipmap.ic_launcher);
                                imageView.setImageBitmap(bitmap);
                                addvieW1(imageView,bitmap, finalI +1);
                            }
                        })

                         */
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void addvieW1(ImageView imageView, Bitmap bitmap,int z) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,1000);

        // setting the margin in linearlayout
        params.setMargins(0, 10, 0, 10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageBitmap(bitmap);
        imageView.setId(z);
        imagery1.add(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "image click is"+view.getId(), Toast.LENGTH_SHORT).show();
                code=view.getId();
                changephoto(view.getId());
            }
        });

        // adding the image in layout
        addingPhotosofUsers.addView(imageView);
    }
    private void addvieW2(ImageView imageView, Uri uri,int z) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300,1000);

        // setting the margin in linearlayout
        params.setMargins(0, 10, 0, 10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(uri);
        imageView.setId(z);
        imagery1.add(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "image click is"+view.getId(), Toast.LENGTH_SHORT).show();
                code=view.getId();
                changephoto(view.getId());
            }
        });

        // adding the image in layout
        addingPhotosofUsers.addView(imageView);
    }


    private void changephoto(int z) {
        Toast.makeText(context, "hello"+String.valueOf(z), Toast.LENGTH_SHORT).show();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, z);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(homePageActivity, new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(context, "Permission not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri selectedImageUri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(selectedImageUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri selectedImageUri = data.getData();
                if (code == 0) {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG" + timeStamp + "." + getFileExt(selectedImageUri);
                    Toast.makeText(context, "what"+String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    imageView.setImageURI(selectedImageUri);
                    imageView.setImageURI(selectedImageUri);
                    addvieW2(imageView,selectedImageUri,imagery1.size()+1);
                    updatephoto(selectedImageUri,imagery1.size()-1);

                } else {
                    imagery1.get(requestCode - 1).setImageURI(selectedImageUri);
                    updatephoto(selectedImageUri,requestCode-1);
                }
            }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //homePageActivity.getSupportFragmentManager().setFragmentFacto;
        //getActivity().set=(HomePageActivity) homePageActivity.getSupportFragmentManager();
    }
}