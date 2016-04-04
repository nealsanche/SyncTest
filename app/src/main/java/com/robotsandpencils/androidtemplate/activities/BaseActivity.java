package com.robotsandpencils.androidtemplate.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Activity base class that contains font handling, simple transitions, and
 * permissions handling.
 *
 * Created by nealsanche on 2016-01-05.
 */
public class BaseActivity extends RxAppCompatActivity {
    public Map<Integer, Runnable> mPermissionsRequests = new HashMap<>();
    public Map<Integer, Runnable> mPermissionsDeniedHandlers = new HashMap<>();

    /**
     * Wrap the context for the font handling to work.
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void beginFade(ViewGroup view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(view, new Fade());
        }
    }

    public void beginTransition(ViewGroup view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(view);
        }
    }

    public void checkPermissions(int permissionsRequestId, String[] permissions, Runnable action, CanDisplayRationale rationaleHandler, Runnable permissionDeniedHandler) {

        Runnable requestPermissionsAction = () -> ActivityCompat.requestPermissions(this, permissions, permissionsRequestId);

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }

        if (!allGranted) {
            boolean showRationale = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showRationale = true;
                }
            }

            mPermissionsRequests.put(permissionsRequestId, action);
            mPermissionsDeniedHandlers.put(permissionsRequestId, permissionDeniedHandler);

            if (showRationale) {
                rationaleHandler.displayRationale(requestPermissionsAction);
            } else {
                requestPermissionsAction.run();
            }

            return;
        }

        action.run();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (mPermissionsRequests.containsKey(requestCode) && mPermissionsDeniedHandlers.containsKey(requestCode)) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                }
            }
            if (!allGranted) {
                mPermissionsDeniedHandlers.get(requestCode).run();
            } else {
                mPermissionsRequests.get(requestCode).run();
            }
            mPermissionsDeniedHandlers.remove(requestCode);
            mPermissionsRequests.remove(requestCode);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public interface CanDisplayRationale {
        void displayRationale(Runnable requestPermissionsRunnable);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // Just suggest doing a GC here
        try {
            System.gc();
            Timber.d("Low memory, GC requested.");
        } catch (Throwable ignored) {
        }
    }
}
