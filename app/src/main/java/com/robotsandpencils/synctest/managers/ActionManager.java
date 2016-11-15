package com.robotsandpencils.synctest.managers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.widget.Toast;

import com.robotsandpencils.synctest.R;

import javax.inject.Inject;

/**
 * A service for actions like dialing the phone.
 * <p>
 * Created by nealsanche on 15-08-27.
 */
public class ActionManager {

    @Inject
    public ActionManager() {
    }

    public void dialPhone(Context context, String number) {
        String phoneNumber = String.format("tel:%s", number);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context.getApplicationContext(), "Unable to dial", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchWebLink(Activity context, String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
                .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
                .setShowTitle(true)
                .build();

        try {
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Throwable ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                context.startActivity(intent);
            } catch (Throwable ex2) {
                Toast.makeText(context, "Unable to launch URL.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void comingSoon(Context context) {
        Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    public void sendEmailMessage(Activity context, String emailAddress, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});

        try {
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.email_chooser_title)));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, R.string.email_failed, Toast.LENGTH_SHORT).show();
        }
    }

}
