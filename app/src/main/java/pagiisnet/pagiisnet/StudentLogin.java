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

import pagiisnet.pagiisnet.R;

public class StudentLogin extends AppCompatActivity {


    private Button logoutButton;
    private Boolean isFirstRun;
    private Button buttonLogin;

    private EditText userEmail;
    private  EditText userPassword;
    private TextView registerTextView;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //logoutButton = findViewById(R.id.logout);

        /*logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        firebaseAuth = FirebaseAuth.getInstance();

        if( firebaseAuth.getCurrentUser() != null)

            //If The usr Is already signed in just call the prpfile activity/
        {
            //Maps or Profile Activity here
            Intent loginIntent = new Intent(StudentLogin.this,MapsActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();

        }

        buttonLogin = findViewById(R.id.buttonLogin);
        userEmail = findViewById(R.id.loginEmail);
        userPassword = findViewById(R.id.loginPassword);
        registerTextView = findViewById(R.id.registerText);

        progressDialog = new ProgressDialog(this);

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
                Intent intent = new Intent(StudentLogin.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

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
        }else {


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

                                    Intent intent = new Intent(StudentLogin.this, SplashIntroOne.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }else
                                {

                                    Intent registerIntent = new Intent(StudentLogin.this, MapsActivity.class);
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
            Intent loginIntent = new Intent(StudentLogin.this, MapsActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
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
            Intent registerIntent = new Intent(StudentLogin.this,RegisterActivity.class);
            registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(registerIntent);
            finish();
        }
    }
}
