package com.example.demoproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {
    EditText phoneNumber,otp1,otp2,otp3,otp4,otp5,otp6;
    TextView resendotp,textmobilenumbershow;
    MaterialButton sendOtp,verifyOtp;
    static String countrycode="+91";
    static String number=null,otp;
    String VerificationId;
    ImageView backbutton;
    PhoneAuthProvider.ForceResendingToken ResendToken;
    ProgressBar progressBarsendOtp,progressBarverifyOtp;
    FirebaseAuth mAuth;
    CountryCodePicker countryCodePicker;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth= FirebaseAuth.getInstance();
        countryCodePicker=findViewById(R.id.ccp);
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countrycode="+"+selectedCountry.getPhoneCode();
            }
        });
        backbutton=findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        phoneNumber=findViewById(R.id.phoneNumber);
        progressBarsendOtp=findViewById(R.id.progressbar_sending_otp);
        progressBarverifyOtp=findViewById(R.id.progressbar_verify_otp);
        resendotp=findViewById(R.id.textresendotp);
        textmobilenumbershow=findViewById(R.id.textmobilenumbershow);
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp("resend");
            }
        });
        otp1=findViewById(R.id.otp1);
        otp2=findViewById(R.id.otp2);
        otp3=findViewById(R.id.otp3);
        otp4=findViewById(R.id.otp4);
        otp5=findViewById(R.id.otp5);
        otp6=findViewById(R.id.otp6);

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    otp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otp1.getText().toString().length()==1)     //size as per your requirement
                {
                    otp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendOtp =findViewById(R.id.sendOtp);
        verifyOtp =findViewById(R.id.verifyotp);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number!=null) {
                    otp=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
                    if (otp.length() > 0) {
                        progressBarverifyOtp.setVisibility(View.VISIBLE);
                        verifyOtp.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPhoneActivity.this, "otp is "+otp, Toast.LENGTH_SHORT).show();
                        verifyotp(otp);
                    }
                    else {
                        Toast.makeText(LoginPhoneActivity.this, "Enter the otp to verify it", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginPhoneActivity.this, "Enter the number first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=null;
                number=countrycode+phoneNumber.getText().toString();
                if (phoneNumber.getText().toString().length()>0){
                    Toast.makeText(LoginPhoneActivity.this, number, Toast.LENGTH_SHORT).show();
                    sendOtp("send");
                }
                else {
                    Toast.makeText(LoginPhoneActivity.this, "Enter the Phone number for verification", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void sendOtp(String sendorresend) {
        progressBarsendOtp.setVisibility(View.VISIBLE);
        sendOtp.setVisibility(View.INVISIBLE);
        if(sendorresend=="send") {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(number)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
        else if(sendorresend=="resend"){
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(number)
                            .setTimeout(5L, TimeUnit.SECONDS)
                            .setActivity(this)
                            .setCallbacks(mCallbacks)
                            .setForceResendingToken(ResendToken)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code !=null){
                verifyotp(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginPhoneActivity.this, "Verification Failed,Try Again", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            super.onCodeSent(verificationId,token);
            ResendToken=token;
            VerificationId=verificationId;
            progressBarsendOtp.setVisibility(View.INVISIBLE);
            sendOtp.setVisibility(View.VISIBLE);
            textmobilenumbershow.setText("Please enter the 6-digit OTP sent to "+ number);
            Toast.makeText(LoginPhoneActivity.this, "Code Sent to your number "+ number, Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyotp(String code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificationId,code);
        signinbyCredential(credential);

    }

    private void signinbyCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    checkUserDetails(task.getResult());
                                    Toast.makeText(LoginPhoneActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }
    private void checkUserDetails(String token) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
            reference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        reference.child(currentUser.getUid()).child("chattoken").setValue(token);
                        Toast.makeText(LoginPhoneActivity.this, snapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(LoginPhoneActivity.this,SwipingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent= new Intent(LoginPhoneActivity.this,RegisterationDetails1.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(LoginPhoneActivity.this,RegisterationScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}