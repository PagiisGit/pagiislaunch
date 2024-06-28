package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashIntroThree extends AppCompatActivity {


    private Boolean isFirstRun;
    private ImageView skipOne, skipTwo;

    private static final int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_intro_three);

        skipOne = findViewById(R.id.skipOne);
        skipTwo = findViewById(R.id.skipTwo);

        skipOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.skipOne);

                Intent registerIntent = new Intent(SplashIntroThree.this, MainActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(registerIntent);
                finish();

            }
        });


        skipTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.findViewById(R.id.skipTwo);

                Intent registerIntent = new Intent(SplashIntroThree.this, MainActivity.class);
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
                    Intent registerIntent = new Intent(SplashIntroThree.this, MainActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(registerIntent);
                    finish();

                }else
                {


                    Intent registerIntent = new Intent(SplashIntroThree.this, MapsActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    registerIntent.putExtra("fromSplash","yes");
                    startActivity(registerIntent);
                    finish();

                }

            }
        },SPLASH_TIME_OUT);
    }
}
