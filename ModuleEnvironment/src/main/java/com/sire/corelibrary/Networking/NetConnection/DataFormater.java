package com.sire.corelibrary.Networking.NetConnection;

public interface DataFormater<T> {
    T reformData(String response);
}