package pagiisnet.pagiisnet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import pagiisnet.pagiisnet.Utils.SliderAdaptor;

public class OnBoarding extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;

    private TextView[] dots;

    private String finish;


    private Button previous;
    private Button next;

    private Button finish2;


    private SliderAdaptor sliderAdaptor;

    private int mCurrentPage;

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            mCurrentPage = position;

            if (position == 0) {

                previous.setEnabled(false);
                next.setEnabled(true);
                previous.setVisibility(View.INVISIBLE);
                finish = "";
                finish2.setVisibility(View.INVISIBLE);

                next.setText("Next");
                previous.setText("");

            } else if (position == dots.length - 1) {

                previous.setEnabled(true);
                next.setEnabled(true);
                previous.setVisibility(View.VISIBLE);
                finish2.setVisibility(View.VISIBLE);

                next.setText("Finish");
                next.setVisibility(View.INVISIBLE);
                finish = "finish";
                previous.setText("Back");
            } else {
                previous.setEnabled(true);
                next.setEnabled(true);
                previous.setVisibility(View.VISIBLE);
                finish2.setVisibility(View.INVISIBLE);

                next.setText("Next");
                finish = "";
                previous.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.PageViewer);
        linearLayout = findViewById(R.id.dotedLayout);

        sliderAdaptor = new SliderAdaptor(this);

        previous = findViewById(R.id.button);
        finish2 = findViewById(R.id.button3);
        finish2.setVisibility(View.INVISIBLE);
        next = findViewById(R.id.button2);
        previous.setVisibility(View.INVISIBLE);

        viewPager.setAdapter(sliderAdaptor);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(mCurrentPage - 1);

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage + 1) ;

            }
        });


        finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(mCurrentPage + 1) ;

                if (next.getText() == "Finish")
                {
                    Intent intent = new Intent(OnBoarding.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });



    }

    public void addDotsIndicator(int position) {

        dots = new TextView[3];
        linearLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.EndBlue));

            linearLayout.addView(dots[i]);

        }

        if (dots.length > 0) {

            dots[position].setTextColor(getResources().getColor(R.color.bluelight));

        }

    }


}