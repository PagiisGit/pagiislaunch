package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import pagiisnet.pagiisnet.R;

public class SplashIntroOne extends AppCompatActivity {

    private Boolean isFirstRun;

    private TextView skipOne;

    private static final int SPLASH_TIME_OUT = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_intro_one);


        skipOne = findViewById(R.id.skip);

        skipOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.skip);

                Intent registerIntent = new Intent(SplashIntroOne.this, SplashIntroTwo.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(registerIntent);
                finish();

            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {

                isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);

                if(isFirstRun)
                {

                    Intent registerIntent = new Intent(SplashIntroOne.this, SplashIntroTwo.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(registerIntent);
                    finish();

                }else
                {

                    Intent registerIntent = new Intent(SplashIntroOne.this, MapsActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(registerIntent);
                    finish();

                }

            }
        },SPLASH_TIME_OUT);
    }

}
