// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.varunest.sparkbutton.SparkButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class BottomSheetProfileServiceBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final TextView addToFavouriteTextView;

  @NonNull
  public final LinearLayoutCompat bottomSheeContainer;

  @NonNull
  public final SparkButton likes;

  @NonNull
  public final ImageView mapsItemProfile;

  @NonNull
  public final TextView popLocationTexview;

  @NonNull
  public final TextView popUpDescriptionTextView;

  @NonNull
  public final TextView popUpDescriptionTextViewThreee;

  @NonNull
  public final TextView popUpDescriptionTextViewTwo;

  @NonNull
  public final TextView share;

  @NonNull
  public final AppCompatImageView shareImageView;

  @NonNull
  public final Button visitProfile;

  private BottomSheetProfileServiceBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull TextView addToFavouriteTextView, @NonNull LinearLayoutCompat bottomSheeContainer,
      @NonNull SparkButton likes, @NonNull ImageView mapsItemProfile,
      @NonNull TextView popLocationTexview, @NonNull TextView popUpDescriptionTextView,
      @NonNull TextView popUpDescriptionTextViewThreee,
      @NonNull TextView popUpDescriptionTextViewTwo, @NonNull TextView share,
      @NonNull AppCompatImageView shareImageView, @NonNull Button visitProfile) {
    this.rootView = rootView;
    this.addToFavouriteTextView = addToFavouriteTextView;
    this.bottomSheeContainer = bottomSheeContainer;
    this.likes = likes;
    this.mapsItemProfile = mapsItemProfile;
    this.popLocationTexview = popLocationTexview;
    this.popUpDescriptionTextView = popUpDescriptionTextView;
    this.popUpDescriptionTextViewThreee = popUpDescriptionTextViewThreee;
    this.popUpDescriptionTextViewTwo = popUpDescriptionTextViewTwo;
    this.share = share;
    this.shareImageView = shareImageView;
    this.visitProfile = visitProfile;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static BottomSheetProfileServiceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BottomSheetProfileServiceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.bottom_sheet_profile_service, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BottomSheetProfileServiceBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addToFavouriteTextView;
      TextView addToFavouriteTextView = ViewBindings.findChildViewById(rootView, id);
      if (addToFavouriteTextView == null) {
        break missingId;
      }

      LinearLayoutCompat bottomSheeContainer = (LinearLayoutCompat) rootView;

      id = R.id.likes;
      SparkButton likes = ViewBindings.findChildViewById(rootView, id);
      if (likes == null) {
        break missingId;
      }

      id = R.id.mapsItemProfile;
      ImageView mapsItemProfile = ViewBindings.findChildViewById(rootView, id);
      if (mapsItemProfile == null) {
        break missingId;
      }

      id = R.id.popLocationTexview;
      TextView popLocationTexview = ViewBindings.findChildViewById(rootView, id);
      if (popLocationTexview == null) {
        break missingId;
      }

      id = R.id.popUpDescriptionTextView;
      TextView popUpDescriptionTextView = ViewBindings.findChildViewById(rootView, id);
      if (popUpDescriptionTextView == null) {
        break missingId;
      }

      id = R.id.popUpDescriptionTextViewThreee;
      TextView popUpDescriptionTextViewThreee = ViewBindings.findChildViewById(rootView, id);
      if (popUpDescriptionTextViewThreee == null) {
        break missingId;
      }

      id = R.id.popUpDescriptionTextViewTwo;
      TextView popUpDescriptionTextViewTwo = ViewBindings.findChildViewById(rootView, id);
      if (popUpDescriptionTextViewTwo == null) {
        break missingId;
      }

      id = R.id.share;
      TextView share = ViewBindings.findChildViewById(rootView, id);
      if (share == null) {
        break missingId;
      }

      id = R.id.shareImageView;
      AppCompatImageView shareImageView = ViewBindings.findChildViewById(rootView, id);
      if (shareImageView == null) {
        break missingId;
      }

      id = R.id.visitProfile;
      Button visitProfile = ViewBindings.findChildViewById(rootView, id);
      if (visitProfile == null) {
        break missingId;
      }

      return new BottomSheetProfileServiceBinding((LinearLayoutCompat) rootView,
          addToFavouriteTextView, bottomSheeContainer, likes, mapsItemProfile, popLocationTexview,
          popUpDescriptionTextView, popUpDescriptionTextViewThreee, popUpDescriptionTextViewTwo,
          share, shareImageView, visitProfile);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
