package com.example.demoproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

public class RegisterationDetails4 extends AppCompatActivity {
    static String gender;
    static int noofphotos=1,flag=1;
    CardView cardView2,cardView3,cardView4,cardView5,cardView6,cardView7;
    StorageReference storage;
    ArrayList<ImageView> imageViews,imageViewslower;
    ImageView imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,backbutton4;
    ImageView imageview2lower,imageview3lower,imageview4lower,imageview5lower,imageview6lower,imageview7lower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViews=new ArrayList<>();
        imageViewslower=new ArrayList<>();
        imageview2lower=findViewById(R.id.imageview2_lower);
        imageViewslower.add(imageview2lower);
        imageview3lower=findViewById(R.id.imageview3_lower);
        imageViewslower.add(imageview3lower);
        imageview4lower=findViewById(R.id.imageview4_lower);
        imageViewslower.add(imageview4lower);
        imageview5lower=findViewById(R.id.imageview5_lower);
        imageViewslower.add(imageview5lower);
        imageview6lower=findViewById(R.id.imageview6_lower);
        imageViewslower.add(imageview6lower);
        imageview7lower=findViewById(R.id.imageview7_lower);
        imageViewslower.add(imageview7lower);
        setContentView(R.layout.activity_registeration_details4);
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        backbutton4=findViewById(R.id.backbutton4);
        backbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cardView2=findViewById(R.id.photo2);
        cardView3=findViewById(R.id.photo3);
        cardView4=findViewById(R.id.photo4);
        cardView5=findViewById(R.id.photo5);
        cardView6=findViewById(R.id.photo6);
        cardView7=findViewById(R.id.photo7);
        imageView2=findViewById(R.id.imageview2);
        imageViews.add(imageView2);
        imageView3=findViewById(R.id.imageview3);
        imageViews.add(imageView3);
        imageView4=findViewById(R.id.imageview4);
        imageViews.add(imageView4);
        imageView5=findViewById(R.id.imageview5);
        imageViews.add(imageView5);
        imageView6=findViewById(R.id.imageview6);
        imageViews.add(imageView6);
        imageView7=findViewById(R.id.imageview7);
        imageViews.add(imageView7);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=2;
                takePictureFromGallery();
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=3;
                takePictureFromGallery();
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=4;
                takePictureFromGallery();
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=5;
                takePictureFromGallery();
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=6;
                takePictureFromGallery();
            }
        });
        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=7;
                takePictureFromGallery();
            }
        });

        MaterialButton Next =findViewById(R.id.NEXT4);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noofphotos>=2)
                {
                    Intent intent = new Intent(RegisterationDetails4.this, RegisterationDetails5.class);
                    startActivity(intent);
                }
                else {
                    //Toast.makeText(RegisterationDetails4.this, "Add Atleast 1 more photo to move further", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    private void takePictureFromGallery() {
        if(noofphotos<7) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        } else {
            //Toast.makeText(this, "You can add 7 photos only on your profile", Toast.LENGTH_SHORT).show();
        }
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
                ImageView imageView = new ImageView(RegisterationDetails4.this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                Toast.makeText(this, "Please wait 1-2 seconds while loading the image", Toast.LENGTH_SHORT).show();
                uploadimage(selectedImageUri,imageView);
            }
        }
    }

    private void uploadimage(Uri imguri, ImageView imageView){
        String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        int photonumber,imageviewnumber;
        if (flag<noofphotos){
            photonumber=flag;
            imageviewnumber=flag-2;

        } else {
            photonumber=noofphotos+1;
            imageviewnumber=noofphotos-1;
        }
        StorageReference reference=storage.child(currentUid+"Profile Picture"+(photonumber));
        reference.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                imageViews.get(imageviewnumber).setImageURI(imguri);
                                imageViewslower.get(imageviewnumber).setImageDrawable(getDrawable(R.drawable.ic_baseline_highlight_off_24));
                                //Toast.makeText(RegisterationDetails4.this, "Image succesfully uploaded", Toast.LENGTH_SHORT).show();
                                if (imageviewnumber==noofphotos-1) {
                                    noofphotos++;
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterationDetails4.this, e.toString()+"Image not Uploaded,check your internet connection and try again!", Toast.LENGTH_LONG).show();
                        Log.e("info", "onFailure: "+e.toString());

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, RegisterationDetails3.class);
        startActivity(intent);
        finish();
    }
}