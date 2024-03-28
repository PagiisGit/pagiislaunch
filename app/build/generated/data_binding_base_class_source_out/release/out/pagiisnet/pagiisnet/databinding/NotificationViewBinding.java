// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class NotificationViewBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final RelativeLayout TitleId;

  @NonNull
  public final ImageView itemViewProfilepic;

  @NonNull
  public final TextView siroccoBrandModel;

  @NonNull
  public final ImageView siroccoImageView;

  private NotificationViewBinding(@NonNull CardView rootView, @NonNull RelativeLayout TitleId,
      @NonNull ImageView itemViewProfilepic, @NonNull TextView siroccoBrandModel,
      @NonNull ImageView siroccoImageView) {
    this.rootView = rootView;
    this.TitleId = TitleId;
    this.itemViewProfilepic = itemViewProfilepic;
    this.siroccoBrandModel = siroccoBrandModel;
    this.siroccoImageView = siroccoImageView;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static NotificationViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NotificationViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.notification_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NotificationViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.TitleId;
      RelativeLayout TitleId = ViewBindings.findChildViewById(rootView, id);
      if (TitleId == null) {
        break missingId;
      }

      id = R.id.item_view_profilepic;
      ImageView itemViewProfilepic = ViewBindings.findChildViewById(rootView, id);
      if (itemViewProfilepic == null) {
        break missingId;
      }

      id = R.id.sirocco_brand_model;
      TextView siroccoBrandModel = ViewBindings.findChildViewById(rootView, id);
      if (siroccoBrandModel == null) {
        break missingId;
      }

      id = R.id.sirocco_image_view;
      ImageView siroccoImageView = ViewBindings.findChildViewById(rootView, id);
      if (siroccoImageView == null) {
        break missingId;
      }

      return new NotificationViewBinding((CardView) rootView, TitleId, itemViewProfilepic,
          siroccoBrandModel, siroccoImageView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
