package com.yjy.fragmentevent.lifemanager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;


import com.yjy.fragmentevent.EventObject;
import com.yjy.fragmentevent.EventPool;
import com.yjy.fragmentevent.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/24
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class EventLifeManager implements Handler.Callback{
    @VisibleForTesting
    static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
    private static final String TAG = "RMRetriever";

    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;

    // Hacks based on the implementation of FragmentManagerImpl in the non-support libraries that
    // allow us to iterate over and retrieve all active Fragments in a FragmentManager.
    private static final String FRAGMENT_INDEX_KEY = "key";



    /**
     * Pending adds for RequestManagerFragments.
     */
    @SuppressWarnings("deprecation")
    @VisibleForTesting
    final Map<android.app.FragmentManager, RequestManagerFragment> pendingRequestManagerFragments =
            new HashMap<>();

    /**
     * Pending adds for SupportRequestManagerFragments.
     */
    @VisibleForTesting
    final Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments =
            new HashMap<>();

    /**
     * Main thread handler to handle cleaning up pending fragment maps.
     */
    private final Handler handler;


    private final Bundle tempBundle = new Bundle();

    public EventLifeManager() {
        handler = new Handler(Looper.getMainLooper(), this /* Callback */);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private <T extends EventFragment,K extends EventObject> T getApplicationManager(@NonNull Context context, Class<K> event) {
        // Either an application context or we're on a background thread.
        T fragment = (T)EventPool.getInstance().get(context,event);
        if(fragment != null){
            return fragment;
        }
        Class<T> clazz = Utils.getEventHandlerClass(event);
        fragment = Utils.getEventFragment(clazz,context,new ApplicationLifecycle());
        EventPool.getInstance().put(context,event,fragment);
        return fragment;
    }

    @NonNull
    public <T extends  EventFragment,K extends EventObject> T get(@NonNull Context context,Class<K> event) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        } else if (Utils.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return get((FragmentActivity) context,event);
            } else if (context instanceof Activity) {
                return get((Activity) context,event);
            } else if (context instanceof ContextWrapper) {
                return get(((ContextWrapper) context).getBaseContext(),event);
            }
        }

        return getApplicationManager(context,event);
    }

    @NonNull
    public <T extends EventFragment,K extends EventObject> T get(@NonNull FragmentActivity activity, Class<K> event) {
        if (Utils.isOnBackgroundThread()) {
            return get(activity.getApplicationContext(),event);
        } else {
            assertNotDestroyed(activity);
            FragmentManager fm = activity.getSupportFragmentManager();
            return supportFragmentGet(
                    activity, fm, /*parentHint=*/ null, isActivityVisible(activity),event);
        }
    }

    @NonNull
    public <T extends EventFragment,K extends EventObject> T get(@NonNull Fragment fragment, Class<K> event) {

        if (Utils.isOnBackgroundThread()) {
            return get(fragment.getActivity().getApplicationContext(),event);
        } else {
            FragmentManager fm = fragment.getChildFragmentManager();
            return supportFragmentGet(fragment.getActivity(), fm, fragment, fragment.isVisible(),event);
        }
    }

    @SuppressWarnings("deprecation")
    @NonNull
    public <T extends  EventFragment,K extends EventObject> T get(@NonNull Activity activity,Class<K> event) {
        if (Utils.isOnBackgroundThread()) {
            return get(activity.getApplicationContext(),event);
        } else {
            assertNotDestroyed(activity);
            android.app.FragmentManager fm = activity.getFragmentManager();
            return fragmentGet(
                    activity, fm, /*parentHint=*/ null, isActivityVisible(activity),event);
        }
    }






    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public <T extends EventFragment,K extends EventObject> T get(@NonNull android.app.Fragment fragment, Class<K> event) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException(
                    "You cannot start a load on a fragment before it is attached");
        }
        if (Utils.isOnBackgroundThread() || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return get(fragment.getActivity().getApplicationContext(),event);
        } else {
            android.app.FragmentManager fm = fragment.getChildFragmentManager();
            return fragmentGet(fragment.getActivity(), fm, fragment, fragment.isVisible(),event);
        }
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @NonNull
    RequestManagerFragment getRequestManagerFragment(Activity activity) {
        return getRequestManagerFragment(
                activity.getFragmentManager(), /*parentHint=*/ null, isActivityVisible(activity));
    }


    @NonNull
    SupportRequestManagerFragment getSupportRequestManagerFragment(FragmentActivity activity) {
        return getSupportRequestManagerFragment(
                activity.getSupportFragmentManager(), /*parentHint=*/ null, isActivityVisible(activity));
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private RequestManagerFragment getRequestManagerFragment(
            @NonNull final android.app.FragmentManager fm,
            @Nullable android.app.Fragment parentHint,
            boolean isParentVisible) {
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingRequestManagerFragments.get(fm);
            if (current == null) {
                current = new RequestManagerFragment();
                current.setParentFragmentHint(parentHint);
                if (isParentVisible) {
                    current.getLifeCycle().onStart();
                }
                pendingRequestManagerFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    @SuppressWarnings({"deprecation", "DeprecatedIsStillUsed,unchecked"})
    @Deprecated
    @NonNull
    private <T extends  EventFragment,K extends EventObject> T fragmentGet(@NonNull Context context,
                                       @NonNull android.app.FragmentManager fm,
                                       @Nullable android.app.Fragment parentHint,
                                       boolean isParentVisible,Class<K> event) {



        RequestManagerFragment current = getRequestManagerFragment(fm, parentHint, isParentVisible);

        //获取fragment中已经注册的对象
        T fragment = (T)EventPool.getInstance().get(current,event);
        if(fragment != null){
            return fragment;
        }

        Class<T> clazz = Utils.getEventHandlerClass(event);
        fragment = Utils.getEventFragment(clazz,context,current.getLifeCycle());
        EventPool.getInstance().put(current,event,fragment);
        return fragment;
    }


    private static boolean isActivityVisible(Activity activity) {
        // This is a poor heuristic, but it's about all we have. We'd rather err on the side of visible
        // and start requests than on the side of invisible and ignore valid requests.
        return !activity.isFinishing();
    }

    @NonNull
    private SupportRequestManagerFragment getSupportRequestManagerFragment(
            @NonNull final FragmentManager fm, @Nullable Fragment parentHint, boolean isParentVisible) {
        SupportRequestManagerFragment current =
                (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingSupportRequestManagerFragments.get(fm);
            if (current == null) {
                current = new SupportRequestManagerFragment();
                current.setParentFragmentHint(parentHint);
                if (isParentVisible) {
                    current.getLifeCycle().onStart();
                }
                pendingSupportRequestManagerFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private <T extends EventFragment,K extends EventObject> T supportFragmentGet(
            @NonNull Context context,
            @NonNull FragmentManager fm,
            @Nullable Fragment parentHint,
            boolean isParentVisible,
            Class<K> event) {
        SupportRequestManagerFragment current =
                getSupportRequestManagerFragment(fm, parentHint, isParentVisible);

        T fragment = (T)EventPool.getInstance().get(current,event);
        if(fragment != null){
            return fragment;
        }


        Class<T> clazz = Utils.getEventHandlerClass(event);
        fragment = Utils.getEventFragment(clazz,context,current.getLifeCycle());
        EventPool.getInstance().put(current,event,fragment);
        return fragment;
    }

    @Override
    public boolean handleMessage(Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case ID_REMOVE_FRAGMENT_MANAGER:
                android.app.FragmentManager fm = (android.app.FragmentManager) message.obj;
                key = fm;
                removed = pendingRequestManagerFragments.remove(fm);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                FragmentManager supportFm = (FragmentManager) message.obj;
                key = supportFm;
                removed = pendingSupportRequestManagerFragments.remove(supportFm);
                break;
            default:
                handled = false;
                break;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }



}
