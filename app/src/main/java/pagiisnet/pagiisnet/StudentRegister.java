package pagiisnet.pagiisnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pagiisnet.pagiisnet.R;

public class StudentRegister extends AppCompatActivity {


    private DatabaseReference storeUserDefaultDataRef;

    private Button registerButton;
    private TextView loginText;
    private EditText userEmail;
    private EditText userPassword;

    private ProgressDialog progressDialog;

    private  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.buttonRegister);
        loginText = findViewById(R.id.loginText);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);

        progressDialog = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent setProfile = new Intent(StudentRegister.this, MapsActivity.class);
            startActivity(setProfile);
        }


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(loginText)) {
                    // Will Open LOgin Activity
                    Intent intent = new Intent(StudentRegister.this,LoginActivity.class);
                    startActivity(intent);
                    finish(); //recently added

                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(registerButton)) {
                    registerUser();
                }

            }


        });
    }

    //Register Method

    private void registerUser() {
        String uEmail = userEmail.getText().toString().trim();
        String uPassword = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(uEmail)  | TextUtils.isEmpty(uPassword)) {
            //Print Emial Is Empty
            Toast.makeText(this, "Both correct user credentials are needed for registering !", Toast.LENGTH_SHORT).show();
        }else {

            //if Credentials Are Correct
            progressDialog.setMessage("PAGiiS Registering user ...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(uEmail, uPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //String deviceToken = FirebaseInstanceId.getInstance().getToken();
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
                                storeUserDefaultDataRef.child("instagramLink").setValue("nulll");


                                //storeUserDefaultDataRef.child("User_Meme").setValue("UserMeme");
                                //storeUserDefaultDataRef.child("device_token").setValue(deviceToken);  //We Gonna use This Later On
                                storeUserDefaultDataRef.child("userThumbImageDp").setValue("userDefaultDp")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)

                                            {

                                                if (task.isSuccessful()) {

                                                    String UserId = firebaseAuth.getCurrentUser().getUid();
                                                    Intent firstTimeProfile = new Intent(StudentRegister.this, first_time_profile_config.class);
                                                    firstTimeProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(firstTimeProfile);
                                                    firstTimeProfile.putExtra("currentLogedInUser", UserId);

                                                }
                                            }
                                        });


                            } else {

                                progressDialog.dismiss();
                                Toast.makeText(StudentRegister.this, " PAGiiS Could Not Regisger user, please try again", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            Intent goToMaps = new Intent(StudentRegister.this, MapsActivity.class);
            startActivity(goToMaps);

        }

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(loginText)) {
                    // Will Open LOgin Activity
                    Intent intent = new Intent(StudentRegister.this,LoginActivity.class);
                    startActivity(intent);

                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(registerButton)) {
                    registerUser();
                }

            }
        });
    }

}
