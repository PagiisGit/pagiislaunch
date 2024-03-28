// Generated by view binder compiler. Do not edit!
package pagiisnet.pagiisnet.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import pagiisnet.pagiisnet.R;

public final class ActivityTabsBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final AppBarLayout appbar;

  @NonNull
  public final BottomNavigationView bottomNav;

  @NonNull
  public final ViewPager container;

  @NonNull
  public final RelativeLayout mainContent;

  @NonNull
  public final TabItem tabItem;

  @NonNull
  public final TabLayout tabs;

  @NonNull
  public final Toolbar toolbar;

  private ActivityTabsBinding(@NonNull RelativeLayout rootView, @NonNull AppBarLayout appbar,
      @NonNull BottomNavigationView bottomNav, @NonNull ViewPager container,
      @NonNull RelativeLayout mainContent, @NonNull TabItem tabItem, @NonNull TabLayout tabs,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.appbar = appbar;
    this.bottomNav = bottomNav;
    this.container = container;
    this.mainContent = mainContent;
    this.tabItem = tabItem;
    this.tabs = tabs;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityTabsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityTabsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_tabs, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityTabsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appbar;
      AppBarLayout appbar = ViewBindings.findChildViewById(rootView, id);
      if (appbar == null) {
        break missingId;
      }

      id = R.id.bottomNav;
      BottomNavigationView bottomNav = ViewBindings.findChildViewById(rootView, id);
      if (bottomNav == null) {
        break missingId;
      }

      id = R.id.container;
      ViewPager container = ViewBindings.findChildViewById(rootView, id);
      if (container == null) {
        break missingId;
      }

      RelativeLayout mainContent = (RelativeLayout) rootView;

      id = R.id.tabItem;
      TabItem tabItem = ViewBindings.findChildViewById(rootView, id);
      if (tabItem == null) {
        break missingId;
      }

      id = R.id.tabs;
      TabLayout tabs = ViewBindings.findChildViewById(rootView, id);
      if (tabs == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityTabsBinding((RelativeLayout) rootView, appbar, bottomNav, container,
          mainContent, tabItem, tabs, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
