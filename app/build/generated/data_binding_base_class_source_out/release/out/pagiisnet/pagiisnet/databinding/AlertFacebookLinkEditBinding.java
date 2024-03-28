// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public final class AlertFacebookLinkEditBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final EditText facebookLinkEdit;

  @NonNull
  public final ImageView fbIcon;

  @NonNull
  public final TextView instruction;

  @NonNull
  public final ImageView saveStatus;

  private AlertFacebookLinkEditBinding(@NonNull CardView rootView,
      @NonNull EditText facebookLinkEdit, @NonNull ImageView fbIcon, @NonNull TextView instruction,
      @NonNull ImageView saveStatus) {
    this.rootView = rootView;
    this.facebookLinkEdit = facebookLinkEdit;
    this.fbIcon = fbIcon;
    this.instruction = instruction;
    this.saveStatus = saveStatus;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static AlertFacebookLinkEditBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AlertFacebookLinkEditBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.alert_facebook_link_edit, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AlertFacebookLinkEditBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.facebookLinkEdit;
      EditText facebookLinkEdit = ViewBindings.findChildViewById(rootView, id);
      if (facebookLinkEdit == null) {
        break missingId;
      }

      id = R.id.fbIcon;
      ImageView fbIcon = ViewBindings.findChildViewById(rootView, id);
      if (fbIcon == null) {
        break missingId;
      }

      id = R.id.instruction;
      TextView instruction = ViewBindings.findChildViewById(rootView, id);
      if (instruction == null) {
        break missingId;
      }

      id = R.id.saveStatus;
      ImageView saveStatus = ViewBindings.findChildViewById(rootView, id);
      if (saveStatus == null) {
        break missingId;
      }

      return new AlertFacebookLinkEditBinding((CardView) rootView, facebookLinkEdit, fbIcon,
          instruction, saveStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
