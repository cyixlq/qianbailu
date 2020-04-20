package com.test.qianbailu.model;


import com.test.qianbailu.model.bean.NoHostException;
import com.test.qianbailu.utils.StringExtKt;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class NoHostRetry<R> implements Function<Observable<Throwable>, ObservableSource<R>> {

    private ApiService apiService;
    private Observable<R> observable;

    public NoHostRetry(ApiService apiService, Observable<R> observable) {
        this.apiService = apiService;
        this.observable = observable;
    }

    @Override
    public ObservableSource<R> apply(Observable<Throwable> throwableObservable) {
        return throwableObservable.flatMap((Function<Throwable, ObservableSource<R>>) throwable -> {
            if (throwable instanceof NoHostException) {
                return apiService.getHost().flatMap((Function<ResponseBody, ObservableSource<R>>) responseBody -> {
                    ConstKt.setHOST(StringExtKt.html2Host(responseBody.string()));
                    return observable;
                });
            } else {
                return Observable.error(throwable);
            }
        });
    }
}
