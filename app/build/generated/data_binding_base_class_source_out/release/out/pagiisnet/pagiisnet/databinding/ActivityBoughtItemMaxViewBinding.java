// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public final class ActivityBoughtItemMaxViewBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView ChooseSlip;

  @NonNull
  public final ImageView DeliverylocationButton;

  @NonNull
  public final ImageView OnlineShop;

  @NonNull
  public final ImageView OnlineShopReceipt;

  @NonNull
  public final ImageView SirrocoMaxProfile;

  @NonNull
  public final ProgressBar SirrocoMaxProgressBar;

  @NonNull
  public final AppBarLayoutBinding chatbarLayout;

  @NonNull
  public final LinearLayout deliveryInforView;

  @NonNull
  public final TextView deliveryLocationTextview;

  @NonNull
  public final ImageView originalPic;

  @NonNull
  public final ImageView profileEmail;

  @NonNull
  public final TextView profileEmailTextBrand;

  @NonNull
  public final ImageView profileNameIcon;

  @NonNull
  public final TextView profileNameTextModel;

  @NonNull
  public final ImageView profileProffession;

  @NonNull
  public final TextView profileProffessionText;

  @NonNull
  public final ImageView profileProffessionThree;

  @NonNull
  public final ImageView profileProffessionTwo;

  @NonNull
  public final Button readyForDelivery;

  @NonNull
  public final ImageView sale;

  @NonNull
  public final TextView siroccoMaxLikes;

  @NonNull
  public final TextView siroccoMaxView;

  @NonNull
  public final ImageView verifySlip;

  private ActivityBoughtItemMaxViewBinding(@NonNull CardView rootView,
      @NonNull ImageView ChooseSlip, @NonNull ImageView DeliverylocationButton,
      @NonNull ImageView OnlineShop, @NonNull ImageView OnlineShopReceipt,
      @NonNull ImageView SirrocoMaxProfile, @NonNull ProgressBar SirrocoMaxProgressBar,
      @NonNull AppBarLayoutBinding chatbarLayout, @NonNull LinearLayout deliveryInforView,
      @NonNull TextView deliveryLocationTextview, @NonNull ImageView originalPic,
      @NonNull ImageView profileEmail, @NonNull TextView profileEmailTextBrand,
      @NonNull ImageView profileNameIcon, @NonNull TextView profileNameTextModel,
      @NonNull ImageView profileProffession, @NonNull TextView profileProffessionText,
      @NonNull ImageView profileProffessionThree, @NonNull ImageView profileProffessionTwo,
      @NonNull Button readyForDelivery, @NonNull ImageView sale, @NonNull TextView siroccoMaxLikes,
      @NonNull TextView siroccoMaxView, @NonNull ImageView verifySlip) {
    this.rootView = rootView;
    this.ChooseSlip = ChooseSlip;
    this.DeliverylocationButton = DeliverylocationButton;
    this.OnlineShop = OnlineShop;
    this.OnlineShopReceipt = OnlineShopReceipt;
    this.SirrocoMaxProfile = SirrocoMaxProfile;
    this.SirrocoMaxProgressBar = SirrocoMaxProgressBar;
    this.chatbarLayout = chatbarLayout;
    this.deliveryInforView = deliveryInforView;
    this.deliveryLocationTextview = deliveryLocationTextview;
    this.originalPic = originalPic;
    this.profileEmail = profileEmail;
    this.profileEmailTextBrand = profileEmailTextBrand;
    this.profileNameIcon = profileNameIcon;
    this.profileNameTextModel = profileNameTextModel;
    this.profileProffession = profileProffession;
    this.profileProffessionText = profileProffessionText;
    this.profileProffessionThree = profileProffessionThree;
    this.profileProffessionTwo = profileProffessionTwo;
    this.readyForDelivery = readyForDelivery;
    this.sale = sale;
    this.siroccoMaxLikes = siroccoMaxLikes;
    this.siroccoMaxView = siroccoMaxView;
    this.verifySlip = verifySlip;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBoughtItemMaxViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBoughtItemMaxViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_bought_item_max_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBoughtItemMaxViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ChooseSlip;
      ImageView ChooseSlip = ViewBindings.findChildViewById(rootView, id);
      if (ChooseSlip == null) {
        break missingId;
      }

      id = R.id.DeliverylocationButton;
      ImageView DeliverylocationButton = ViewBindings.findChildViewById(rootView, id);
      if (DeliverylocationButton == null) {
        break missingId;
      }

      id = R.id.OnlineShop;
      ImageView OnlineShop = ViewBindings.findChildViewById(rootView, id);
      if (OnlineShop == null) {
        break missingId;
      }

      id = R.id.OnlineShopReceipt;
      ImageView OnlineShopReceipt = ViewBindings.findChildViewById(rootView, id);
      if (OnlineShopReceipt == null) {
        break missingId;
      }

      id = R.id.SirrocoMaxProfile;
      ImageView SirrocoMaxProfile = ViewBindings.findChildViewById(rootView, id);
      if (SirrocoMaxProfile == null) {
        break missingId;
      }

      id = R.id.SirrocoMaxProgressBar;
      ProgressBar SirrocoMaxProgressBar = ViewBindings.findChildViewById(rootView, id);
      if (SirrocoMaxProgressBar == null) {
        break missingId;
      }

      id = R.id.chatbarLayout;
      View chatbarLayout = ViewBindings.findChildViewById(rootView, id);
      if (chatbarLayout == null) {
        break missingId;
      }
      AppBarLayoutBinding binding_chatbarLayout = AppBarLayoutBinding.bind(chatbarLayout);

      id = R.id.deliveryInforView;
      LinearLayout deliveryInforView = ViewBindings.findChildViewById(rootView, id);
      if (deliveryInforView == null) {
        break missingId;
      }

      id = R.id.deliveryLocationTextview;
      TextView deliveryLocationTextview = ViewBindings.findChildViewById(rootView, id);
      if (deliveryLocationTextview == null) {
        break missingId;
      }

      id = R.id.originalPic;
      ImageView originalPic = ViewBindings.findChildViewById(rootView, id);
      if (originalPic == null) {
        break missingId;
      }

      id = R.id.profileEmail;
      ImageView profileEmail = ViewBindings.findChildViewById(rootView, id);
      if (profileEmail == null) {
        break missingId;
      }

      id = R.id.profileEmailTextBrand;
      TextView profileEmailTextBrand = ViewBindings.findChildViewById(rootView, id);
      if (profileEmailTextBrand == null) {
        break missingId;
      }

      id = R.id.profileNameIcon;
      ImageView profileNameIcon = ViewBindings.findChildViewById(rootView, id);
      if (profileNameIcon == null) {
        break missingId;
      }

      id = R.id.profileNameTextModel;
      TextView profileNameTextModel = ViewBindings.findChildViewById(rootView, id);
      if (profileNameTextModel == null) {
        break missingId;
      }

      id = R.id.profileProffession;
      ImageView profileProffession = ViewBindings.findChildViewById(rootView, id);
      if (profileProffession == null) {
        break missingId;
      }

      id = R.id.profileProffessionText;
      TextView profileProffessionText = ViewBindings.findChildViewById(rootView, id);
      if (profileProffessionText == null) {
        break missingId;
      }

      id = R.id.profileProffessionThree;
      ImageView profileProffessionThree = ViewBindings.findChildViewById(rootView, id);
      if (profileProffessionThree == null) {
        break missingId;
      }

      id = R.id.profileProffessionTwo;
      ImageView profileProffessionTwo = ViewBindings.findChildViewById(rootView, id);
      if (profileProffessionTwo == null) {
        break missingId;
      }

      id = R.id.readyForDelivery;
      Button readyForDelivery = ViewBindings.findChildViewById(rootView, id);
      if (readyForDelivery == null) {
        break missingId;
      }

      id = R.id.sale;
      ImageView sale = ViewBindings.findChildViewById(rootView, id);
      if (sale == null) {
        break missingId;
      }

      id = R.id.siroccoMaxLikes;
      TextView siroccoMaxLikes = ViewBindings.findChildViewById(rootView, id);
      if (siroccoMaxLikes == null) {
        break missingId;
      }

      id = R.id.siroccoMaxView;
      TextView siroccoMaxView = ViewBindings.findChildViewById(rootView, id);
      if (siroccoMaxView == null) {
        break missingId;
      }

      id = R.id.verifySlip;
      ImageView verifySlip = ViewBindings.findChildViewById(rootView, id);
      if (verifySlip == null) {
        break missingId;
      }

      return new ActivityBoughtItemMaxViewBinding((CardView) rootView, ChooseSlip,
          DeliverylocationButton, OnlineShop, OnlineShopReceipt, SirrocoMaxProfile,
          SirrocoMaxProgressBar, binding_chatbarLayout, deliveryInforView, deliveryLocationTextview,
          originalPic, profileEmail, profileEmailTextBrand, profileNameIcon, profileNameTextModel,
          profileProffession, profileProffessionText, profileProffessionThree,
          profileProffessionTwo, readyForDelivery, sale, siroccoMaxLikes, siroccoMaxView,
          verifySlip);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
