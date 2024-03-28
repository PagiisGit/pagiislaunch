package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Verify_Cellphonenumber_Login extends AppCompatActivity {


    private String Phonenunber;
    private EditText OTP;
    private Button verifyOTP;
    private ProgressBar progressBarOTP;
    private TextView resendOTP;

    private DatabaseReference storeUserDefaultDataRef;

    private String verificationCode;

    private PhoneAuthProvider.ForceResendingToken ResendingToken;


    private FirebaseAuth firebaseAuth;
    private Long timeStamp = 60L;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_cellphonenumber_login);

        FirebaseApp.initializeApp(getApplicationContext());

        OTP = findViewById(R.id.loginOTP);
        verifyOTP = findViewById(R.id.buttonLogin);
        progressBarOTP = findViewById(R.id.progressBarOTP);
        resendOTP = findViewById(R.id.resendOTP);

        Phonenunber = getIntent().getExtras().getString("phone");

        firebaseAuth = FirebaseAuth.getInstance();


        sendOTP(Phonenunber,false);

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                String entredOTP = OTP.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,entredOTP);
                signIn(credential);

            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendOTP(Phonenunber,true);

            }
        });

    }

    void sendOTP(String phoneNumber,boolean isSend)
    {

        startResendTimer();

        setInprogress(true);

        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeStamp, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks()

                {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                    {
                        signIn(phoneAuthCredential);
                        setInprogress(false);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e)
                    {

                        Toast.makeText(Verify_Cellphonenumber_Login.this, "Failed sending OTP", Toast.LENGTH_SHORT).show();
                        setInprogress(false);

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        ResendingToken = forceResendingToken;
                        Toast.makeText(Verify_Cellphonenumber_Login.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                        setInprogress(false);
                    }
                });
        if(isSend)

        {

            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());


        }else

        {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());

        }

    }

    private void startResendTimer()

    {
        resendOTP.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                timeStamp--;
                resendOTP.setText("Resending OTP in "+timeStamp+"seconds");

                if(timeStamp<=0)

                {
                    timeStamp = 60L;
                    timer.cancel();

                    runOnUiThread(() -> {

                        resendOTP.setEnabled(true);


                    });

                }

            }
        },0,1000);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential)

    {

        setInprogress(true);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                setInprogress(false);

                if(task.isSuccessful())

                {

                    final String currentUserId = firebaseAuth.getCurrentUser().getUid();
                    storeUserDefaultDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                    //storeUserDefaultDataRef.child(currentUserId).setValue(currentUserId);
                    //We gonna user the username Email Instead for now but Later on we should be able to change and store inside the actual userName
                    storeUserDefaultDataRef.child("userNameAsEmail").setValue("UserName");
                    storeUserDefaultDataRef.child("admin").setValue(false);
                    storeUserDefaultDataRef.child("full_name").setValue("Full Name"); //new
                    storeUserDefaultDataRef.child("email_address").setValue("Email Adress");//new
                    storeUserDefaultDataRef.child("proffession").setValue("Proffession");//new
                    storeUserDefaultDataRef.child("mobile_number").setValue("Mobile");//new
                    storeUserDefaultDataRef.child("userDefaultStatus").setValue(" Hi, Welcome to Pagiis. ");
                    storeUserDefaultDataRef.child("userImageDp").setValue("userDefaultDp");
                    storeUserDefaultDataRef.child("Age").setValue("nulll");

                    storeUserDefaultDataRef.child("facebookLink").setValue("null");
                    storeUserDefaultDataRef.child("twitterLink").setValue("null");
                    storeUserDefaultDataRef.child("instagramLink").setValue("nulll").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                            Intent intent = new Intent(Verify_Cellphonenumber_Login.this,first_time_profile_config.class);
                            intent.putExtra("phone", Phonenunber);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });


                }else
                {

                    Toast.makeText(Verify_Cellphonenumber_Login.this, "OTP verification failed", Toast.LENGTH_SHORT).show();


                }

            }
        });


    }



    private void checkIfUserExists(String phoneNumber) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = usersRef.orderByChild("phoneNumber").equalTo(phoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    // User already exists
                    // Handle the case when the user already exists

                    Intent intent = new Intent(Verify_Cellphonenumber_Login.this,MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                } else {
                    // User doesn't exist
                    // Handle the case when the user doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Handle any errors



            }
        });
    }


    void setInprogress(boolean isInProgress)

    {

        if(isInProgress)
        {

            progressBarOTP.setVisibility(View.VISIBLE);
            verifyOTP.setVisibility(View.GONE);

        }else

        {

            progressBarOTP.setVisibility(View.INVISIBLE);
            verifyOTP.setVisibility(View.VISIBLE);

        }

    }
}