package com.swun.tinama.aidl_demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.swun.tinama.aidl_demo.Service.CarManagerService;
import com.swun.tinama.aidl_demo.utils.MyUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler();
    private ICarFactoryManager mTmpFactoryManager;

    private IOnNewCarArrivedListener carArrivedListener = new IOnNewCarArrivedListener.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewCarArrived(final Car car) throws RemoteException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "接收到新的车的通知啦!" + car.toString());
                }
            });
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ICarFactoryManager carFactoryManager = ICarFactoryManager.Stub.asInterface(service);
            try {
                mTmpFactoryManager = carFactoryManager;
                List<Car> cars = carFactoryManager.getCars();
                printCarInformation(cars);
                cars.add(new Car(3, "奔驰"));
                printCarInformation(cars);
                carFactoryManager.registListener(carArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    void printCarInformation(List<Car> cars) {
        Log.i(TAG, cars.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, MyUtils.getCurrentProcessName(this));
    }

    @Override
    protected void onDestroy() {
        /*if (mTmpFactoryManager != null && mTmpFactoryManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG,"注销Listener!");
                mTmpFactoryManager.unRegistListener(carArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }*/
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    public void UNREGIST(View v) {
        if (mTmpFactoryManager != null && mTmpFactoryManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "注销Listener!");
                mTmpFactoryManager.unRegistListener(carArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void CLICK(View v) {
        Intent intent = new Intent(this, CarManagerService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }
}
