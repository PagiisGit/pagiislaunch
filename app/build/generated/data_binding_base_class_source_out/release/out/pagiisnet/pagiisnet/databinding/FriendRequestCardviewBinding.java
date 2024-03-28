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
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class FriendRequestCardviewBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final Button friendRequestAccept;

  @NonNull
  public final Button friendRequestDecline;

  @NonNull
  public final ImageView friendRequestImageView;

  @NonNull
  public final TextView friendRequestUserName;

  private FriendRequestCardviewBinding(@NonNull CardView rootView,
      @NonNull Button friendRequestAccept, @NonNull Button friendRequestDecline,
      @NonNull ImageView friendRequestImageView, @NonNull TextView friendRequestUserName) {
    this.rootView = rootView;
    this.friendRequestAccept = friendRequestAccept;
    this.friendRequestDecline = friendRequestDecline;
    this.friendRequestImageView = friendRequestImageView;
    this.friendRequestUserName = friendRequestUserName;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static FriendRequestCardviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FriendRequestCardviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.friend_request_cardview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FriendRequestCardviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.friendRequestAccept;
      Button friendRequestAccept = ViewBindings.findChildViewById(rootView, id);
      if (friendRequestAccept == null) {
        break missingId;
      }

      id = R.id.friendRequestDecline;
      Button friendRequestDecline = ViewBindings.findChildViewById(rootView, id);
      if (friendRequestDecline == null) {
        break missingId;
      }

      id = R.id.friendRequestImageView;
      ImageView friendRequestImageView = ViewBindings.findChildViewById(rootView, id);
      if (friendRequestImageView == null) {
        break missingId;
      }

      id = R.id.friendRequestUserName;
      TextView friendRequestUserName = ViewBindings.findChildViewById(rootView, id);
      if (friendRequestUserName == null) {
        break missingId;
      }

      return new FriendRequestCardviewBinding((CardView) rootView, friendRequestAccept,
          friendRequestDecline, friendRequestImageView, friendRequestUserName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
