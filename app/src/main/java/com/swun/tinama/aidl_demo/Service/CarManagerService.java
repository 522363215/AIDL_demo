package com.swun.tinama.aidl_demo.Service;


import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;
import android.util.Log;

import com.swun.tinama.aidl_demo.Car;
import com.swun.tinama.aidl_demo.ICarFactoryManager;
import com.swun.tinama.aidl_demo.IOnNewCarArrivedListener;
import com.swun.tinama.aidl_demo.utils.MyUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/5/8.
 */
public class CarManagerService extends Service {

    private static final String TAG = "CarManangerService";
    private AtomicBoolean mAtomicBoolean = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Car> mCarList = new CopyOnWriteArrayList<Car>();
    //    private CopyOnWriteArrayList<IOnNewCarArrivedListener> mListeners = new CopyOnWriteArrayList<IOnNewCarArrivedListener>();
    private RemoteCallbackList<IOnNewCarArrivedListener> mListeners = new RemoteCallbackList<IOnNewCarArrivedListener>();
    private IBinder binder = new ICarFactoryManager.Stub() {
        @Override
        public void addCar(Car car) throws RemoteException {
            mCarList.add(car);
        }

        @Override
        public void remove(Car car) throws RemoteException {
            mCarList.remove(car);
        }

        @Override
        public List<Car> getCars() throws RemoteException {
            return mCarList;
        }

        @Override
        public void registListener(IOnNewCarArrivedListener listener) throws RemoteException {
//            mListeners.add(listener);
            mListeners.register(listener);
        }

        @Override
        public void unRegistListener(IOnNewCarArrivedListener listener) throws RemoteException {
//            mListeners.remove(listener);
            mListeners.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mCarList.add(new Car(1, "奥迪"));
        mCarList.add(new Car(2, "大众"));
        Log.i(TAG, MyUtils.getCurrentProcessName(this));
        new Thread(new FactoryWroker()).start();
    }

    private class FactoryWroker implements Runnable {

        @Override
        public void run() {
            while (!mAtomicBoolean.get()) {
                try {
                    Thread.sleep(1000);
                    int carID = mCarList.size() + 1;
                    Car car = new Car(carID, "new car#" + carID);
                    onNewCarArrived(car);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void onNewCarArrived(Car car) throws RemoteException {
        Log.i(TAG, "new car arrived" + car.toString());
        mCarList.add(car);
        /*for (IOnNewCarArrivedListener listener : mListeners) {
            listener.onNewCarArrived(car);
        }*/
        int count = mListeners.beginBroadcast();
        for (int i = 0; i < count; i++) {
            IOnNewCarArrivedListener listener = mListeners.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewCarArrived(car);
            }
        }
        mListeners.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        mAtomicBoolean.set(true);
        super.onDestroy();
    }
}
