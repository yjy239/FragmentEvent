package com.yjy.fragmentevent.lifemanager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.yjy.fragmentevent.EventPool;
import com.yjy.fragmentevent.FragmentEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**

 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class RequestManagerFragment extends Fragment {
  private static final String TAG = "RMFragment";
  private final ActivityFragmentLifecycle lifecycle;

  @SuppressWarnings("deprecation")
  private final Set<RequestManagerFragment> childRequestManagerFragments = new HashSet<>();


  @SuppressWarnings("deprecation")
  @Nullable
  private RequestManagerFragment rootRequestManagerFragment;
  @Nullable private Fragment parentFragmentHint;

  public RequestManagerFragment() {
    this(new ActivityFragmentLifecycle());
  }

  @VisibleForTesting
  @SuppressLint("ValidFragment")
  RequestManagerFragment(@NonNull ActivityFragmentLifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }



  @NonNull
  ActivityFragmentLifecycle getLifeCycle() {
    return lifecycle;
  }



  @SuppressWarnings("deprecation")
  private void addChildRequestManagerFragment(RequestManagerFragment child) {
    childRequestManagerFragments.add(child);
  }

  @SuppressWarnings("deprecation")
  private void removeChildRequestManagerFragment(RequestManagerFragment child) {
    childRequestManagerFragments.remove(child);
  }



  /**
   * Sets a hint for which fragment is our parent which allows the fragment to return correct
   * information about its parents before pending fragment transactions have been executed.
   */
  void setParentFragmentHint(@Nullable Fragment parentFragmentHint) {
    this.parentFragmentHint = parentFragmentHint;
    if (parentFragmentHint != null && parentFragmentHint.getActivity() != null) {
      registerFragmentWithRoot(parentFragmentHint.getActivity());
    }
  }

  @Nullable
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  private Fragment getParentFragmentUsingHint() {
    final Fragment fragment;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      fragment = getParentFragment();
    } else {
      fragment = null;
    }
    return fragment != null ? fragment : parentFragmentHint;
  }



  @SuppressWarnings("deprecation")
  private void registerFragmentWithRoot(@NonNull Activity activity) {
    unregisterFragmentWithRoot();
    //获取根部
    rootRequestManagerFragment =
            FragmentEvent.getDefault().getLifeManager().getRequestManagerFragment(activity);
    if (!equals(rootRequestManagerFragment)) {
      rootRequestManagerFragment.addChildRequestManagerFragment(this);
    }
  }

  private void unregisterFragmentWithRoot() {
    if (rootRequestManagerFragment != null) {
      rootRequestManagerFragment.removeChildRequestManagerFragment(this);
      rootRequestManagerFragment = null;
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      registerFragmentWithRoot(activity);
    } catch (IllegalStateException e) {
      // OnAttach can be called after the activity is destroyed, see #497.
      if (Log.isLoggable(TAG, Log.WARN)) {
        Log.w(TAG, "Unable to register fragment with root", e);
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    unregisterFragmentWithRoot();
  }

  @Override
  public void onStart() {
    super.onStart();
    lifecycle.onStart();
  }

  @Override
  public void onStop() {
    super.onStop();
    lifecycle.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    lifecycle.onDestroy();
    unregisterFragmentWithRoot();

    //清空EventPool的数据
    EventPool.getInstance().recycle(this);
  }

  @Override
  public String toString() {
    return super.toString() + "{parent=" + getParentFragmentUsingHint() + "}";
  }


}
