package com.elprog.taskandroid;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elprog.taskandroid.model.Item;
import com.elprog.taskandroid.repo.MainRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainRepository mainRepository;
    private MutableLiveData<List<Item>> vediosResponseMutableLiveData = new MutableLiveData<>();

    public MainViewModel() {
        mainRepository = new MainRepository();
    }

    public LiveData<List<Item>> RequestVediosAndPdf() {

        Single<List<Item>> single = mainRepository.RequestVediosAndPdf();
        compositeDisposable.add(single.subscribe(o -> vediosResponseMutableLiveData.postValue(o), e -> Log.d("TAG", "RequestVediosAndPdf: " + e)));
        return vediosResponseMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }

    }
}
