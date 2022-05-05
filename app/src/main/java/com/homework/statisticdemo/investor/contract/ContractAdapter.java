package com.homework.statisticdemo.investor.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.statisticdemo.R;
import com.homework.statisticdemo.model.Date;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {
    Context context;
    List<Date> list;
    Listener listener;

    public ContractAdapter(Context con, List<Date> list) {
        this.context = con;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contract_card_view_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date item = list.get(position);

        holder.amount.setText(item.getAmount());
        holder.percent.setText(item.getPercent());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, percent, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.contract_amount);
            percent = itemView.findViewById(R.id.contract_percent);
            date = itemView.findViewById(R.id.contract_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (listener != null){
                        listener.onClick(pos);
                    }
                }
            });
        }
    }
}
