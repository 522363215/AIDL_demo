// IOnNewCarArrivedListener.aidl
package com.swun.tinama.aidl_demo;
import com.swun.tinama.aidl_demo.Car;
// Declare any non-default types here with import statements

interface IOnNewCarArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void onNewCarArrived(in Car car);
}
