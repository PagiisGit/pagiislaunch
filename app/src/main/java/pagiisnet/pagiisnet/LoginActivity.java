package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private TextView forgotPassword;

    private Boolean isFirstRun;

    private ImageView mobileSingUpImageView;
    private TextView mobileSignUpTextView;

    private TextView buttonLogin;
    private EditText userEmail;
    private  EditText userPassword;
    private TextView registerTextView;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        //logoutButton = findViewById(R.id.logout);

        /*logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        firebaseAuth = FirebaseAuth.getInstance();
        /*if( firebaseAuth.getCurrentUser() != null) //If The usr Is already signed in just call the prpfile activity/
        {
            //Maps or Profile Activity here
            Intent loginIntent = new Intent(LoginActivity.this,MapsActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();

        }*/

        buttonLogin = findViewById(R.id.buttonLogin);
        userEmail = findViewById(R.id.loginEmail);
        userPassword = findViewById(R.id.loginPassword);
        registerTextView = findViewById(R.id.registerText);

        forgotPassword = findViewById(R.id.forgotPasswordButton);

        progressDialog = new ProgressDialog(this);

        mobileSignUpTextView = findViewById(R.id.mobileSIgnUpText);
        mobileSingUpImageView = findViewById(R.id.mobileSignUpImage);


        //buttonLogin.setOnClickListener((View.OnClickListener) this);
        //registerTextView.setOnClickListener((View.OnClickListener) this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (view.equals(buttonLogin))
                {
                    // Will Open LOgin Activity
                    UserLogin();

                }

            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.registerText);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });

        mobileSingUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(mobileSingUpImageView)) {
                    // Will Open LOgin Activity
                    Intent intent = new Intent(LoginActivity.this,CellphoneLogin.class);
                    startActivity(intent);
                    finish(); //recently added

                }

            }
        });

        mobileSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.equals(mobileSignUpTextView)) {
                    // Will Open LOgin Activity
                    Intent intent = new Intent(LoginActivity.this,CellphoneLogin.class);
                    startActivity(intent);
                    finish(); //recently added

                }

            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.findViewById(R.id.forgotPasswordButton);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.forgortpassword_edit, null);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText emailField = mView.findViewById(R.id.emailContact);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText cellPhoneNumber = mView.findViewById(R.id.cancelSignOutImage);
                ImageButton sendEmail = mView.findViewById(R.id.sendEmail);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                sendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!emailField.getText().toString().isEmpty())

                        {

                            String email = emailField.getText().toString();
                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Toast.makeText(LoginActivity.this, "Email Sent Successfully, please check your emails for password reset link", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();

                                }
                            });

                        }

                    }
                });

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

                } else
                {
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

    //Declare The UserLogin Method

    private void UserLogin()
    {

        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email) | TextUtils.isEmpty(password)) {
            //Print Emial Is Empty
            Toast.makeText(this, "Both correct user credentials are required to login", Toast.LENGTH_SHORT).show();
        }else
        {

            //if Credentials Are Correct
            progressDialog.setMessage("Logging In  user ...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();
                            if (task.isSuccessful()) {


                                isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);

                                if(isFirstRun)
                                {

                                    Intent intent = new Intent(LoginActivity.this, OnBoarding.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }else
                                {

                                    Intent registerIntent = new Intent(LoginActivity.this, MapsActivity.class);
                                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(registerIntent);
                                    finish();

                                }


                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null) //If The usr Is already signed in just call the prpfile activity/
        {
            Intent loginIntent = new Intent(LoginActivity.this,MapsActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        }
    }

    //@Override
    public void onClick(View view) {

        if(view == buttonLogin)
        {
            UserLogin();
        }
        if(view ==registerTextView)
        {
            Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
            registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(registerIntent);
            finish();
        }
    }
}
