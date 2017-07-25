package com.goldtek.database;

import java.util.List;

public interface IDbCallback<T> {
    void onQuery(List<T> list);
}
