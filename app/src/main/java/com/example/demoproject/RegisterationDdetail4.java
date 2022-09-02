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

public class RegisterationDdetail4 extends AppCompatActivity {

    ImageView profile_view,add_pic;
    EditText name,DateOfBirth;
    CardView dob_view;
    TextView male,female;
    Spinner other;

    private static final int REQUEST_IMAGE_CAPTURE = 2;
    static int noOfImage = 0;
    static ArrayList<Uri> imagery1;
    String currentPhotoPath;
    StorageReference storage;
    MaterialButton next;
    File f;
    static String userName;

    DatePickerDialog.OnDateSetListener setListener;
    static int age;
    static String dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerationdetail4);
        profile_view=findViewById(R.id.profilepic);
        add_pic=findViewById(R.id.add_pic);
        name=findViewById(R.id.namE);
        DateOfBirth=findViewById(R.id.dob);
        dob_view=findViewById(R.id.dob_view);
        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);
        dob_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegisterationDdetail4.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
                return false;
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
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        other.setAdapter(myAdapter);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setTextColor(Color.parseColor("#E15454"));
                male.setBackgroundResource(R.drawable.background_box_gender);
                female.setTextColor(getResources().getColor(com.google.android.material.R.color.m3_default_color_secondary_text));
                //other.setTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_secondary));
                female.setBackgroundResource(R.drawable.background_box_gener1);
                other.setBackgroundResource(R.drawable.background_box_gener1);

            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setTextColor(Color.parseColor("#E15454"));
                female.setBackgroundResource(R.drawable.background_box_gender);
                male.setTextColor(getResources().getColor(com.google.android.material.R.color.m3_default_color_secondary_text));
                //other.setTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_secondary));
                male.setBackgroundResource(R.drawable.background_box_gener1);
                other.setBackgroundResource(R.drawable.background_box_gener1);

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setTextColor(Color.parseColor("#E15454"));
                other.setBackgroundResource(R.drawable.background_box_gender);
                female.setTextColor(getResources().getColor(com.google.android.material.R.color.m3_default_color_secondary_text));
                //other.setTextColor(getResources().getColor(com.google.android.material.R.color.design_default_color_secondary));
                female.setBackgroundResource(R.drawable.background_box_gener1);
                male.setBackgroundResource(R.drawable.background_box_gener1);

            }
        });
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        next = findViewById(R.id.next4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(Calendar.getInstance().getTime());
                String yy = date.substring(date.length() - 4);
                String d1= DateOfBirth.getText().toString();
                String yyuser= d1.substring(d1.length()-4);
                age= Integer.parseInt(yy) - Integer.parseInt(yyuser);
                /*if (DateOfBirth.getText().toString().length()>0) {
                    if (age>=18) {
                        dob = DateOfBirth.getText().toString();
                        Intent intent = new Intent(RegisterationDetails3.this, RegisterationDetails4.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterationDetails3.this, "You must be above or equal to 18 to register", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(RegisterationDetails3.this, "Enter your Date of Birth to move furthur", Toast.LENGTH_SHORT).show();
                }

                 */
                if (noOfImage>=1) {
                    userName=name.getText().toString();
                    if(name.getText().length()>0) {
                        if (DateOfBirth.getText().toString().length()>0) {
                            if (age>=18) {
                                dob = DateOfBirth.getText().toString();
                                Intent intent = new Intent(RegisterationDdetail4.this, RegisterationDetails8.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterationDdetail4.this, "You must be above or equal to 18 to register", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            Toast.makeText(RegisterationDdetail4.this, "Enter your Date of Birth to move furthur", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterationDdetail4.this, "Enter Your Name to move further", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterationDdetail4.this, "Add profile photo to move further", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imagery1 = new ArrayList<>(20);
        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noOfImage < 4) {
                    takePictureFromGallery();
                }
                else {
                    Toast.makeText(RegisterationDdetail4.this, "Only 4 images can be added", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(RegisterationDdetail4.this, android.Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(RegisterationDdetail4.this, new String[]{Manifest.permission.CAMERA}, 20);
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
            Toast.makeText(RegisterationDdetail4.this, "Permission not Granted", Toast.LENGTH_SHORT).show();
        }
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
                ImageView imageView = new ImageView(RegisterationDdetail4.this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                uploadimage(selectedImageUri,imageView);
                noOfImage++;
            }
        }
    }


    private void uploadimage(Uri imguri, ImageView imageView){
        String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference reference=storage.child(currentUid+"Profile Picture"+(noOfImage+1));
        reference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                profile_view.setImageURI(imguri);
                                imagery1.add(imguri);
                                Toast.makeText(RegisterationDdetail4.this, "Image succesfully uploaded", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterationDdetail4.this, e.toString()+"Image not Uploaded,check your internet connection and try again!", Toast.LENGTH_LONG).show();
                        Log.e("info", "onFailure: "+e.toString());

                    }
                });
    }
}