package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeMotionLayout extends AppCompatActivity
{

    private Boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_motion_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()

            {

                isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);

                if(isFirstRun)
                {

                    Intent registerIntent = new Intent(WelcomeMotionLayout.this, RegisterActivity.class);
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(registerIntent);
                    finish();

                }else
                {

                    Intent registerIntent = new Intent(WelcomeMotionLayout.this, MapsActivity.class);
                    registerIntent.putExtra("visit_user_id", "splash");
                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(registerIntent);
                    finish();

                }



            }
        },3000);


    }
}