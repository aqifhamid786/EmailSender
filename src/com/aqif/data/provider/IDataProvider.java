package com.aqif.data.provider;

public interface IDataProvider<T> {

    int recordsCount();
    T[] getRecords(int position, int count);
}
