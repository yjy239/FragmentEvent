package com.yjy.fragmentevent.lifemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.yjy.fragmentevent.EventPool;
import com.yjy.fragmentevent.FragmentEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class SupportRequestManagerFragment extends Fragment {
  private static final String TAG = "SupportRMFragment";
  private final ActivityFragmentLifecycle lifecycle;
  private final Set<SupportRequestManagerFragment> childRequestManagerFragments = new HashSet<>();

  @Nullable private SupportRequestManagerFragment rootRequestManagerFragment;

  @Nullable private Fragment parentFragmentHint;

  public SupportRequestManagerFragment() {
    this(new ActivityFragmentLifecycle());
  }

  @VisibleForTesting
  @SuppressLint("ValidFragment")
  public SupportRequestManagerFragment(@NonNull ActivityFragmentLifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }


  @NonNull
  ActivityFragmentLifecycle getLifeCycle() {
    return lifecycle;
  }



  private void addChildRequestManagerFragment(SupportRequestManagerFragment child) {
    childRequestManagerFragments.add(child);
  }

  private void removeChildRequestManagerFragment(SupportRequestManagerFragment child) {
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
  private Fragment getParentFragmentUsingHint() {
    Fragment fragment = getParentFragment();
    return fragment != null ? fragment : parentFragmentHint;
  }


  private void registerFragmentWithRoot(@NonNull FragmentActivity activity) {
    unregisterFragmentWithRoot();
    rootRequestManagerFragment =
            FragmentEvent.getDefault().getLifeManager().getSupportRequestManagerFragment(activity);
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

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      registerFragmentWithRoot(getActivity());
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
    parentFragmentHint = null;
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
