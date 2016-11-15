package com.robotsandpencils.synctest.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by nealsanche on 2016-01-25.
 */
public class TestUtil {

    private static AtomicBoolean isRunningTest;

    public static synchronized boolean isRunningTest() {
        if (null == isRunningTest) {
            boolean isATest;

            try {
                Class.forName("android.support.test.espresso.Espresso");
                isATest = true;
            } catch (ClassNotFoundException e) {
                isATest = false;
            }

            isRunningTest = new AtomicBoolean(isATest);
        }

        return isRunningTest.get();
    }

}
