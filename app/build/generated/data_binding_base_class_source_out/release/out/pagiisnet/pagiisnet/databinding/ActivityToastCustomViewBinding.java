// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class ActivityToastCustomViewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView customToastImage;

  @NonNull
  public final TextView customToastText;

  private ActivityToastCustomViewBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView customToastImage, @NonNull TextView customToastText) {
    this.rootView = rootView;
    this.customToastImage = customToastImage;
    this.customToastText = customToastText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityToastCustomViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityToastCustomViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_toast_custom_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityToastCustomViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.customToastImage;
      ImageView customToastImage = ViewBindings.findChildViewById(rootView, id);
      if (customToastImage == null) {
        break missingId;
      }

      id = R.id.customToastText;
      TextView customToastText = ViewBindings.findChildViewById(rootView, id);
      if (customToastText == null) {
        break missingId;
      }

      return new ActivityToastCustomViewBinding((LinearLayout) rootView, customToastImage,
          customToastText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
