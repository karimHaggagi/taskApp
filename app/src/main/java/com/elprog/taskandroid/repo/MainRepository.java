package com.elprog.taskandroid.repo;

import com.elprog.taskandroid.db.RestApiService;
import com.elprog.taskandroid.db.RetrofitInstance;
import com.elprog.taskandroid.model.Item;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepository {


    private RetrofitInstance retrofitInstance;
    private RestApiService apiService;

    public MainRepository() {
        retrofitInstance = new RetrofitInstance();
        apiService = retrofitInstance.getApiService();

    }


    public Single<List<Item>> RequestVediosAndPdf() {

        Single<List<Item>> single = apiService.RequestVediosAndPdf()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache();
        return single;
    }
}
