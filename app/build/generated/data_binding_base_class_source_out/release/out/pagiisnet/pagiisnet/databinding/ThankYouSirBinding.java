// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class ThankYouSirBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView homeButotn;

  @NonNull
  public final LinearLayout selectChoseOptionLayout;

  @NonNull
  public final ImageView verifyPurchase;

  private ThankYouSirBinding(@NonNull CardView rootView, @NonNull ImageView homeButotn,
      @NonNull LinearLayout selectChoseOptionLayout, @NonNull ImageView verifyPurchase) {
    this.rootView = rootView;
    this.homeButotn = homeButotn;
    this.selectChoseOptionLayout = selectChoseOptionLayout;
    this.verifyPurchase = verifyPurchase;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ThankYouSirBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ThankYouSirBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.thank_you_sir, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ThankYouSirBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.homeButotn;
      ImageView homeButotn = ViewBindings.findChildViewById(rootView, id);
      if (homeButotn == null) {
        break missingId;
      }

      id = R.id.selectChoseOptionLayout;
      LinearLayout selectChoseOptionLayout = ViewBindings.findChildViewById(rootView, id);
      if (selectChoseOptionLayout == null) {
        break missingId;
      }

      id = R.id.verifyPurchase;
      ImageView verifyPurchase = ViewBindings.findChildViewById(rootView, id);
      if (verifyPurchase == null) {
        break missingId;
      }

      return new ThankYouSirBinding((CardView) rootView, homeButotn, selectChoseOptionLayout,
          verifyPurchase);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
