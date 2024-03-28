package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import pagiisnet.pagiisnet.R;

public class SplashActivity extends AppCompatActivity {


    private Boolean isFirstRun;

    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {

                isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);

                if(isFirstRun)
                {

                    Intent registerIntent = new Intent(SplashActivity.this, RegisterActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(registerIntent);
                    finish();

                }else
                {

                    Intent registerIntent = new Intent(SplashActivity.this, MapsActivity.class);
                    registerIntent.putExtra("visit_user_id", "splash");
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(registerIntent);
                    finish();
                    
                }

            }
        },SPLASH_TIME_OUT);
    }

}
