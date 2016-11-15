package com.robotsandpencils.synctest.activities;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotsandpencils.synctest.App;
import com.robotsandpencils.synctest.TestDaggerRule;
import com.robotsandpencils.synctest.managers.DataManager;
import com.robotsandpencils.synctest.model.Version;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import io.realm.Realm;
import it.cosenonjaviste.daggermock.InjectFromComponent;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest {

    @Rule
    public TestDaggerRule daggerRule = new TestDaggerRule();

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new ActivityTestRule<>(DashboardActivity.class, false, false);

    @Mock
    DataManager mDataManager;

    @InjectFromComponent
    Realm mRealm;

    private App mApp;

    @Before
    public void setUp() throws Exception {
        mApp = TestDaggerRule.getApp();
    }

    @Test
    public void shouldDisplayVersionNumber() {
        assertNotNull("Data manager should not be null.", mDataManager);
        assertNotNull("Realm should not be null.", mRealm);

        Version value = new Version();
        value.version = "10";
        when(mDataManager.getVersion()).thenReturn(Observable.just(value));

        mActivityRule.launchActivity(null);

        onView(withText("Version: 10")).check(matches(isDisplayed()));
    }
}
