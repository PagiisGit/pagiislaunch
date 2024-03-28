package pagiisnet.pagiisnet.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

import pagiisnet.pagiisnet.R;
import pagiisnet.pagiisnet.Art;
import pagiisnet.pagiisnet.Business;
import pagiisnet.pagiisnet.Fashion;
import pagiisnet.pagiisnet.Music;
import pagiisnet.pagiisnet.ViewMyCart;

public class BottomNavHelperSirocco {

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
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.PublicIcon) {
                    Intent mapsActivity = new Intent(mContext, Art.class);
                    mContext.startActivity(mapsActivity);
                } else if (itemId == R.id.profileIcon) {
                    Intent profile = new Intent(mContext, ViewMyCart.class);
                    mContext.startActivity(profile);
                } else if (itemId == R.id.pagiis_view_icon) {
                    Intent upload = new Intent(mContext, Business.class);
                    mContext.startActivity(upload);
                } else if (itemId == R.id.cameraIcon) {
                    Intent music = new Intent(mContext, Music.class);
                    mContext.startActivity(music);

                        /* Intent camera = new Intent(mContext, ActivityCamera.class);
                        mContext.startActivity(camera);
                        break;*/
                } else if (itemId == R.id.Notifications) {
                    Intent notifications = new Intent(mContext, Fashion.class);
                    mContext.startActivity(notifications);
                }

                return false;
            }
        });
    }
}
