package com.swun.tinama.aidl_demo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/5/8.
 */
public class Car implements Parcelable {

    public int getCarID() {
        return carID;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carID=" + carID +
                ", carName='" + carName + '\'' +
                '}';
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    private int carID;
    private String carName;

    protected Car(Parcel in) {
        carID = in.readInt();
        carName = in.readString();
    }

    public Car(int carID, String carName) {
        this.carID = carID;
        this.carName = carName;
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(carID);
        dest.writeString(carName);
    }
}
