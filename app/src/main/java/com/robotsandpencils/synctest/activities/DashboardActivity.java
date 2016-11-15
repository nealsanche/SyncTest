package com.robotsandpencils.synctest.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.robotsandpencils.synctest.App;
import com.robotsandpencils.synctest.R;
import com.robotsandpencils.synctest.databinding.ActivityDashboardBinding;
import com.robotsandpencils.synctest.managers.DataManager;
import com.robotsandpencils.synctest.model.Message;
import com.robotsandpencils.synctest.net.RemoteAPI;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.Date;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityDashboardBinding mBinding;

    @Inject
    DataManager mDataManager;

    @Inject
    Realm mRealm;

    @Inject
    RemoteAPI mRemoteAPI;
    private Message mManagedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

        ((App) getApplication()).getUserComponent().inject(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        Toolbar toolbar = mBinding.appBarInclude.toolbar;
        setSupportActionBar(toolbar);

        FloatingActionButton fab = mBinding.appBarInclude.fab;
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Performing test", Snackbar.LENGTH_LONG)
                    .show();

            performTest();
        });

        DrawerLayout drawer = mBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

        mBinding.appBarInclude.content.setTitleText("Welcome");

        // Empty the realm out initially
        mRealm.executeTransaction(realm -> {
            realm.deleteAll();
        });
    }

    private void performTest() {
        Message message = new Message();
        message.setDate(new Date());
        message.setNew(true);
        message.setTo("neal@nsdev.org");
        message.setId(new Date().getTime());
        message.setSubject("Testing this thing.");
        message.setMesage("The message is to be kept secret at all costs. Never tell Trump.");
        mRealm.executeTransaction(realm -> {
            realm.copyToRealm(message);
        });

        // Find the message in our database
        mManagedMessage = mRealm.where(Message.class).equalTo("id", message.getId()).findFirst();

        mManagedMessage.addChangeListener(element -> {
            Timber.d("Message changed: %s", mManagedMessage.isValid());
        });
        syncMessage(mManagedMessage);
    }

    private void syncMessage(final Message message) {
        Timber.i("Syncing message: %s", message.getId());

        // Need to copy from realm, otherwise network call fails with
        // java.lang.IllegalStateException: Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.
        Observable<Message> messageObservable = mRemoteAPI.syncMessage(mRealm.copyFromRealm(message))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        messageObservable.subscribe(message1 -> {

            Timber.i("Got a message back from the sync post: %s", message1.getId());

            if (message.isNew()) {
                // Delete the old message and replace it with the new one
                mRealm.executeTransaction(realm -> {
                    message.deleteFromRealm();
                    mManagedMessage = realm.copyToRealmOrUpdate(message1);
                });
            }

        }, error -> {
            Timber.e(error, "Error during sync!");
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTitle();
    }

    private void updateTitle() {
        mDataManager.getVersion()
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(version -> {
                            mBinding.appBarInclude.content.setTitleText(String.format("Version: %s", version.version));
                            mBinding.appBarInclude.content.setPresenter(this);
                        },
                        error -> {
                            Toast.makeText(DashboardActivity.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        });
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onTestClick(View view) {
        Timber.d("Test clicked.");
        if (mManagedMessage != null) {
            if (mManagedMessage.isValid()) {
                Snackbar.make(view, "Managed Message Id: " + mManagedMessage.getId(), Snackbar.LENGTH_LONG)
                        .show();
            } else {
                Snackbar.make(view, "Message has been deleted.", Snackbar.LENGTH_LONG)
                        .show();
            }

        }
    }
}
