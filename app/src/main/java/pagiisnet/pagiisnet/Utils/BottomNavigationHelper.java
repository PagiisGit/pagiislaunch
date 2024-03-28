package pagiisnet.pagiisnet.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.ActivityCamera;
import pagiisnet.pagiisnet.ActivityNotifications;
import pagiisnet.pagiisnet.ActivityOwnProfile;
import pagiisnet.pagiisnet.ActivityUploadImage;
import pagiisnet.pagiisnet.LinkPost;
import pagiisnet.pagiisnet.MapsActivity;
import pagiisnet.pagiisnet.MemePage;
import pagiisnet.pagiisnet.ViewUsersMemes;


public class BottomNavigationHelper {

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++)
            {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShifting(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }

    }

    public static  void  enableNavigation(final Context mContext, BottomNavigationView view  )
    {

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.PublicIcon) {
                    Intent mapsActivity = new Intent(mContext, ViewUsersMemes.class);
                    mContext.startActivity(mapsActivity);
                } else if (itemId == R.id.profileIcon) {
                    Intent profile = new Intent(mContext, ActivityOwnProfile.class);
                    mContext.startActivity(profile);
                } else if (itemId == R.id.pagiis_view_icon) {
                    Intent upload = new Intent(mContext, MemePage.class);
                    mContext.startActivity(upload);
                } else if (itemId == R.id.cameraIcon) {
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    View mView = View.inflate(mContext, R.layout.activity_user_post, null);


                    ImageView logoutImage = mView.findViewById(R.id.cameraOption);
                    ImageView postLink = mView.findViewById(R.id.linkEditImageView);
                    ImageView cancelLogOutImage = mView.findViewById(R.id.GalleryOption);
                    ImageView homebutton = mView.findViewById(R.id.homeButotn);


                    postLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            view.findViewById(R.id.linkEditImageView);
                            Intent intent = new Intent(mContext, LinkPost.class);
                            mContext.startActivity(intent);

                        }
                    });


                    homebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            view.findViewById(R.id.homeButotn);
                            Intent intent = new Intent(mContext, MapsActivity.class);
                            mContext.startActivity(intent);


                        }
                    });


                    logoutImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            view.findViewById(R.id.cameraOption);
                            Intent intent = new Intent(mContext, ActivityCamera.class);
                            mContext.startActivity(intent);


                        }
                    });

                    cancelLogOutImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, ActivityUploadImage.class);
                            intent.putExtra("share_item_id", "null");
                            mContext.startActivity(intent);

                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();

                    dialog.show();

                        /* Intent camera = new Intent(mContext, ActivityCamera.class);
                        mContext.startActivity(camera);
                        break;*/
                }
                else if (itemId == R.id.Notifications) {
                    Intent notifications = new Intent(mContext, ActivityNotifications.class);
                    mContext.startActivity(notifications);
                }

                return false;
            }
        });
    }
}
