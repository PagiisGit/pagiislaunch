package pagiisnet.pagiisnet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.hbb20.CountryCodePicker;

public class CellphoneLogin extends AppCompatActivity {



    CountryCodePicker countryCodePicker;
    Button logiButton;
    EditText phoneNumber;
    ProgressBar OTPprogressbar;
    TextView sendingOTP;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellphone_login);


        FirebaseApp.initializeApp(this);

        countryCodePicker = findViewById(R.id.countryCodePicker);
        logiButton = findViewById(R.id.buttonLogin);
        phoneNumber = findViewById(R.id.loginNumber);
        OTPprogressbar = findViewById(R.id.sendOTP);

        OTPprogressbar.setVisibility(View.GONE);



        countryCodePicker.registerCarrierNumberEditText(phoneNumber);
        logiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!countryCodePicker.isValidFullNumber())
                {
                    phoneNumber.setError("Phone number is INVALID! ");
                    return;


                }

                Intent intent = new Intent(CellphoneLogin.this, Verify_Cellphonenumber_Login.class);
                intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);

            }
        });
    }
}