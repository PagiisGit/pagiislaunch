package pagiisnet.pagiisnet.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.mikhaellopez.circularimageview.CircularImageView;

import pagiisnet.pagiisnet.R;

//import pagiisnet.pagiisnet.R;

public class SliderAdaptor extends PagerAdapter {

    public int[] slide_images = {

            R.drawable.pagiis_splash_logo_edit_final_file,

            R.drawable.ic_sirocco_icon,
            R.drawable.astronaut

    };
    public String[] slide_headings = {


            "Pagiis",
            "Sirocco",
            "Welcome"


    };
    public String[] slide_description = {


            "PUBLIC USER INTERACTIONS BASED ON REAL TIME LOCATIONS.",
            "PAGIIS ONLINE MARKETING AND ADVERTISEMENT PLATFORM FOR ONLINE SHOPPING",
            "PAGiiS x SIROCCO is Designed and Created for both public social and Advertisement purposes. The platform integrates two of the most influential ways infomation sharing, PAGiiS being the (Networking House) and Sirocco being the (Online Shopping and Advertisement House). These two platforms aims to serve and deliver valuable user information to and from the public in a most effective and secured way, thus improving the present and future internet-based user interactions. \n" + "\n" + "Founder: MR. ML MLAMBO, Co-Founder: MR. SA NGUBANE"


    };
    Context mContext;
    LayoutInflater layoutInflater;


    public SliderAdaptor(Context context) {


        this.mContext = context;

    }

    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slideview, container, false);


        CircularImageView circularImageView = view.findViewById(R.id.logo);


        TextView textViewHeading = view.findViewById(R.id.Heading);
        TextView textDescription = view.findViewById(R.id.description);

        circularImageView.setImageResource(slide_images[position]);


        textViewHeading.setText(slide_headings[position]);

        textDescription.setText(slide_description[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
