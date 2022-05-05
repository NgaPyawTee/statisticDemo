package com.homework.statisticdemo.investor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.statisticdemo.R;
import com.homework.statisticdemo.model.Investor;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    Context context;
    List<Investor> list;
    Listener listener;

    public CategoryAdapter(Context context, List<Investor> list) {
        this.context = context;
        this.list = list;
    }

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_investor_card_view,parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(list,position,listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
