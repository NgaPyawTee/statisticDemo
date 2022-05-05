package com.homework.statisticdemo.investor.contract;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.statisticdemo.R;
import com.homework.statisticdemo.investor.detail.InvestorDetailActivity;
import com.homework.statisticdemo.model.Date;

import java.util.ArrayList;
import java.util.List;

public class FinalPlanFragment extends Fragment {
    private String bundlePass;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ContractAdapter adapter;
    private List<Date> list = new ArrayList<>();
    private CollectionReference collRef = FirebaseFirestore.getInstance().collection("Investors");

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contract_detail, container, false);

        Bundle bundle = getArguments();
        bundlePass = bundle.getString(InvestorDetailActivity.BUNDLE_PASS);

        progressBar = v.findViewById(R.id.contract_progress_bar);
        recyclerView = v.findViewById(R.id.contract_recycler_view);
        adapter = new ContractAdapter(getActivity(), list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        RetrieveContractOneData();
    }

    private void RetrieveContractOneData() {
        collRef.document(bundlePass)
                .collection("Final Date").orderBy("currentTime", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(getActivity(), new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                list.clear();
                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    Date item = ds.toObject(Date.class);
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}