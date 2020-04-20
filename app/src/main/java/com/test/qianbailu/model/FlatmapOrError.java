package com.test.qianbailu.model;

import android.text.TextUtils;

import com.test.qianbailu.model.bean.NoHostException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class FlatmapOrError<R> implements Function<String, Observable<R>> {

    private Observable<R> observable;

    public FlatmapOrError(Observable<R> observable) {
        this.observable = observable;
    }

    @Override
    public Observable<R> apply(String s) throws Exception {
        if (TextUtils.isEmpty(s)) {
            return Observable.error(new NoHostException());
        } else {
            return this.observable;
        }
    }
}
