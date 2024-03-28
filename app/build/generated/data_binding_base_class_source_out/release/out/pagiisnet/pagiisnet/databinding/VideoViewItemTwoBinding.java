// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class VideoViewItemTwoBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final RelativeLayout TitleId;

  @NonNull
  public final TextView chats;

  @NonNull
  public final ImageView itemViewProfilepic;

  @NonNull
  public final TextView likesOne;

  @NonNull
  public final TextView likesTwo;

  @NonNull
  public final VideoView memeImageView;

  @NonNull
  public final ImageView pagiisRadiusChat;

  @NonNull
  public final ImageView pagiisRadiusFriends;

  @NonNull
  public final TextView postPosition;

  @NonNull
  public final TextView postTitle;

  @NonNull
  public final TextView postUserName;

  @NonNull
  public final RelativeLayout relativeSearcLayout;

  @NonNull
  public final TextView share;

  @NonNull
  public final ImageView siroccoImageViewTumb;

  @NonNull
  public final ProgressBar videoProgressBar;

  @NonNull
  public final TextView viewers;

  @NonNull
  public final ImageView views;

  @NonNull
  public final ImageView volumeControlOff;

  private VideoViewItemTwoBinding(@NonNull RelativeLayout rootView, @NonNull RelativeLayout TitleId,
      @NonNull TextView chats, @NonNull ImageView itemViewProfilepic, @NonNull TextView likesOne,
      @NonNull TextView likesTwo, @NonNull VideoView memeImageView,
      @NonNull ImageView pagiisRadiusChat, @NonNull ImageView pagiisRadiusFriends,
      @NonNull TextView postPosition, @NonNull TextView postTitle, @NonNull TextView postUserName,
      @NonNull RelativeLayout relativeSearcLayout, @NonNull TextView share,
      @NonNull ImageView siroccoImageViewTumb, @NonNull ProgressBar videoProgressBar,
      @NonNull TextView viewers, @NonNull ImageView views, @NonNull ImageView volumeControlOff) {
    this.rootView = rootView;
    this.TitleId = TitleId;
    this.chats = chats;
    this.itemViewProfilepic = itemViewProfilepic;
    this.likesOne = likesOne;
    this.likesTwo = likesTwo;
    this.memeImageView = memeImageView;
    this.pagiisRadiusChat = pagiisRadiusChat;
    this.pagiisRadiusFriends = pagiisRadiusFriends;
    this.postPosition = postPosition;
    this.postTitle = postTitle;
    this.postUserName = postUserName;
    this.relativeSearcLayout = relativeSearcLayout;
    this.share = share;
    this.siroccoImageViewTumb = siroccoImageViewTumb;
    this.videoProgressBar = videoProgressBar;
    this.viewers = viewers;
    this.views = views;
    this.volumeControlOff = volumeControlOff;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static VideoViewItemTwoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static VideoViewItemTwoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.video_view_item_two, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static VideoViewItemTwoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.TitleId;
      RelativeLayout TitleId = ViewBindings.findChildViewById(rootView, id);
      if (TitleId == null) {
        break missingId;
      }

      id = R.id.chats;
      TextView chats = ViewBindings.findChildViewById(rootView, id);
      if (chats == null) {
        break missingId;
      }

      id = R.id.item_view_profilepic;
      ImageView itemViewProfilepic = ViewBindings.findChildViewById(rootView, id);
      if (itemViewProfilepic == null) {
        break missingId;
      }

      id = R.id.likes_one;
      TextView likesOne = ViewBindings.findChildViewById(rootView, id);
      if (likesOne == null) {
        break missingId;
      }

      id = R.id.likes_two;
      TextView likesTwo = ViewBindings.findChildViewById(rootView, id);
      if (likesTwo == null) {
        break missingId;
      }

      id = R.id.memeImageView;
      VideoView memeImageView = ViewBindings.findChildViewById(rootView, id);
      if (memeImageView == null) {
        break missingId;
      }

      id = R.id.pagiis_radius_chat;
      ImageView pagiisRadiusChat = ViewBindings.findChildViewById(rootView, id);
      if (pagiisRadiusChat == null) {
        break missingId;
      }

      id = R.id.pagiis_radius_friends;
      ImageView pagiisRadiusFriends = ViewBindings.findChildViewById(rootView, id);
      if (pagiisRadiusFriends == null) {
        break missingId;
      }

      id = R.id.postPosition;
      TextView postPosition = ViewBindings.findChildViewById(rootView, id);
      if (postPosition == null) {
        break missingId;
      }

      id = R.id.post_title;
      TextView postTitle = ViewBindings.findChildViewById(rootView, id);
      if (postTitle == null) {
        break missingId;
      }

      id = R.id.postUserName;
      TextView postUserName = ViewBindings.findChildViewById(rootView, id);
      if (postUserName == null) {
        break missingId;
      }

      RelativeLayout relativeSearcLayout = (RelativeLayout) rootView;

      id = R.id.share;
      TextView share = ViewBindings.findChildViewById(rootView, id);
      if (share == null) {
        break missingId;
      }

      id = R.id.sirocco_image_view_tumb;
      ImageView siroccoImageViewTumb = ViewBindings.findChildViewById(rootView, id);
      if (siroccoImageViewTumb == null) {
        break missingId;
      }

      id = R.id.videoProgressBar;
      ProgressBar videoProgressBar = ViewBindings.findChildViewById(rootView, id);
      if (videoProgressBar == null) {
        break missingId;
      }

      id = R.id.viewers;
      TextView viewers = ViewBindings.findChildViewById(rootView, id);
      if (viewers == null) {
        break missingId;
      }

      id = R.id.views;
      ImageView views = ViewBindings.findChildViewById(rootView, id);
      if (views == null) {
        break missingId;
      }

      id = R.id.volumeControlOff;
      ImageView volumeControlOff = ViewBindings.findChildViewById(rootView, id);
      if (volumeControlOff == null) {
        break missingId;
      }

      return new VideoViewItemTwoBinding((RelativeLayout) rootView, TitleId, chats,
          itemViewProfilepic, likesOne, likesTwo, memeImageView, pagiisRadiusChat,
          pagiisRadiusFriends, postPosition, postTitle, postUserName, relativeSearcLayout, share,
          siroccoImageViewTumb, videoProgressBar, viewers, views, volumeControlOff);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
