package com.elprog.taskandroid.db;

import com.elprog.taskandroid.model.Item;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface RestApiService {


    @GET("getListOfFilesResponse.json")
    Single<List<Item>> RequestVediosAndPdf();


}
