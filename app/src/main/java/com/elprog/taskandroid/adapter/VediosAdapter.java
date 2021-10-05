package com.elprog.taskandroid.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elprog.taskandroid.R;
import com.elprog.taskandroid.databinding.ListItemBinding;
import com.elprog.taskandroid.model.Item;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VediosAdapter extends RecyclerView.Adapter<VediosAdapter.VediosViewHolder> {
    List<Item> ItemList;
    Context mContext;
    OnItemClickedListner onItemClickedListner;

    public VediosAdapter(OnItemClickedListner onItemClickedListner) {
        ItemList = new ArrayList<>();
        this.onItemClickedListner = onItemClickedListner;
    }

    @NonNull
    @Override
    public VediosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        VediosViewHolder vediosViewHolder = new VediosViewHolder(view);

        mContext = parent.getContext();

        return vediosViewHolder;
    }

    public void setItemList(List<Item> ItemList) {
        this.ItemList = ItemList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VediosViewHolder holder, int position) {
        Item item = ItemList.get(position);
        Object map = loadMap().get("" + item.getId());
        if ( map!= null) {
            holder.binding.done.setVisibility(View.VISIBLE);
            holder.binding.downLoad.setVisibility(View.GONE);
        } else {
            holder.binding.done.setVisibility(View.GONE);
            holder.binding.downLoad.setVisibility(View.VISIBLE);
        }
        if (item.getType().equals("VIDEO")) {

            holder.binding.pdf.setVisibility(View.GONE);
            holder.binding.simpleVideoView.setVisibility(View.VISIBLE);
            HashMap<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("foo", "bar");
            holder.binding.simpleVideoView.setSource(item.getUrl(), extraHeaders);
            holder.binding.simpleVideoView.stopPlayer();
            holder.binding.simpleVideoView.requestFocus();

        } else {
            holder.binding.simpleVideoView.setVisibility(View.GONE);
            holder.binding.pdf.setVisibility(View.VISIBLE);
        }
        holder.binding.downLoad.setOnClickListener(v -> onItemClickedListner.OnItemDownloadClicked(item));
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    private Map<String, Object> loadMap() {
        Map<String, Object> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = mContext.getSharedPreferences("MySharedPreferane",
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

    public interface OnItemClickedListner {
        void OnItemDownloadClicked(Item item);

    }

    public class VediosViewHolder extends RecyclerView.ViewHolder {
        ListItemBinding binding;

        public VediosViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ListItemBinding.bind(itemView);

        }
    }
}
