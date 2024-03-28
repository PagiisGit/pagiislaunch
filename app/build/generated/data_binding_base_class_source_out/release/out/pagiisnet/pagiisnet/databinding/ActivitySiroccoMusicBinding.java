// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class ActivitySiroccoMusicBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final AppBarLayoutBinding chatbarLayout;

  @NonNull
  public final RecyclerView memeRecyclerView;

  @NonNull
  public final ProgressBar progressCircle;

  private ActivitySiroccoMusicBinding(@NonNull RelativeLayout rootView,
      @NonNull AppBarLayoutBinding chatbarLayout, @NonNull RecyclerView memeRecyclerView,
      @NonNull ProgressBar progressCircle) {
    this.rootView = rootView;
    this.chatbarLayout = chatbarLayout;
    this.memeRecyclerView = memeRecyclerView;
    this.progressCircle = progressCircle;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySiroccoMusicBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySiroccoMusicBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sirocco_music, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySiroccoMusicBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.chatbarLayout;
      View chatbarLayout = ViewBindings.findChildViewById(rootView, id);
      if (chatbarLayout == null) {
        break missingId;
      }
      AppBarLayoutBinding binding_chatbarLayout = AppBarLayoutBinding.bind(chatbarLayout);

      id = R.id.memeRecyclerView;
      RecyclerView memeRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (memeRecyclerView == null) {
        break missingId;
      }

      id = R.id.progress_circle;
      ProgressBar progressCircle = ViewBindings.findChildViewById(rootView, id);
      if (progressCircle == null) {
        break missingId;
      }

      return new ActivitySiroccoMusicBinding((RelativeLayout) rootView, binding_chatbarLayout,
          memeRecyclerView, progressCircle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
