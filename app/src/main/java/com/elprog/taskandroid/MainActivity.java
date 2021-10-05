package com.elprog.taskandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.elprog.taskandroid.adapter.VediosAdapter;
import com.elprog.taskandroid.databinding.ActivityMainBinding;
import com.elprog.taskandroid.model.Item;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements VediosAdapter.OnItemClickedListner {

    MainViewModel mainViewModel;
    VediosAdapter vediosAdapter;
    ActivityMainBinding binding;

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        RequestVediosAndPdf();
    }

    private void init() {
        //init viewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //init adapter
        vediosAdapter = new VediosAdapter(this);
        binding.recyclerItems.setAdapter(vediosAdapter);

    }


    /**
     * request data from the server
     */
    private void RequestVediosAndPdf() {
        mainViewModel.RequestVediosAndPdf().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> vediosResponse) {
                if (vediosResponse != null) {

                    vediosAdapter.setItemList(vediosResponse);
                }
            }
        });

    }

    private void DownLoadAlert(Item item) {
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("File Downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    progressBarStatus = progresslevel();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);

                        }
                    });
                }

                if (progressBarStatus >= 100) {


                    try {
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Map<String, Object> map = loadMap();
                            map.put("" + item.getId(), "");
                            saveMap( map);
                            vediosAdapter.notifyDataSetChanged();
                        }
                    });
                    progressBar.dismiss();
                }
            }
        }).start();
    }

    public int progresslevel() {

        while (level <= 100) {

            level++;

            if (level == 10) {
                return 10;
            } else if (level == 20) {
                return 20;
            } else if (level == 30) {
                return 30;
            } else if (level == 40) {
                return 40;
            } else if (level == 50) {
                return 50;
            } else if (level == 60) {
                return 60;
            } else if (level == 70) {
                return 70;
            } else if (level == 80) {
                return 80;
            } else if (level == 90) {
                return 90;
            }


        }

        return 100;

    }


    @Override
    public void OnItemDownloadClicked(Item item) {
        DownLoadAlert(item);
    }

    private void saveMap(Map<String, Object> inputMap) {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MySharedPreferane",
                Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("map").apply();
            editor.putString("map", jsonString);
            editor.commit();
        }
    }


    private Map<String, Object> loadMap() {
        Map<String, Object> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MySharedPreferane",
                Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

}