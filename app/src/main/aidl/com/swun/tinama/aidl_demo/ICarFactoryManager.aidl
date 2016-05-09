// ICarFactory.aidl
package com.swun.tinama.aidl_demo;
import com.swun.tinama.aidl_demo.Car;
import com.swun.tinama.aidl_demo.IOnNewCarArrivedListener;

interface ICarFactoryManager {
    void addCar(in Car car);
    void remove(in Car car);
    List<Car> getCars();
    void registListener(IOnNewCarArrivedListener listener);
    void unRegistListener(IOnNewCarArrivedListener listener);

}
