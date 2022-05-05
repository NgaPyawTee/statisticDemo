package com.homework.statisticdemo.investor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.statisticdemo.R;
import com.homework.statisticdemo.model.Investor;

import java.util.List;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView name, totalAmount, date811;
    int amount1, amount2;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.investor_name);
        totalAmount = itemView.findViewById(R.id.investor_total_amount);
        date811 = itemView.findViewById(R.id.category_date811);

    }

    public void bind(List<Investor> list, int position, CategoryAdapter.Listener listener) {
        Investor item = list.get(position);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        String strName = item.getName();
        String strDate811 = item.getDate811();

        name.setText(strName);
        date811.setText(strDate811);

        String stramount1 = item.getAmount811();
        String stramount2 = item.getAmount58();

        if (stramount1.equals("Unavaliable") && stramount2.equals("Unavaliable")) {
            amount1 = 0;
            amount2 = 0;
        } else if (stramount2.equals("Unavaliable")) {
            amount1 = Integer.parseInt(stramount1);
            amount2 = 0;
        } else if (stramount1.equals("Unavaliable")) {
            amount1 = 0;
            amount2 = Integer.parseInt(stramount2);
        } else {
            amount1 = Integer.parseInt(stramount1);
            amount2 = Integer.parseInt(stramount2);
        }

        CalculateTotalAmount(amount1, amount2);
    }

    private void CalculateTotalAmount(int a, int b) {
        int amountTotal = a + b;

        totalAmount.setText(String.valueOf(amountTotal));
    }

}
