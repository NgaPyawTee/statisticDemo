package com.homework.statisticdemo.investor.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.statisticdemo.R;
import com.homework.statisticdemo.model.Investor;
import com.squareup.picasso.Picasso;

public class DetailHomeFragment extends Fragment {
    private String bundlePass;

    private TextView name, companyID, phone, nrc, address,
            amount811, percent811, date811,
            amount58, percent58, date58,
            amount456, percent456, date456,
            cashAmount, cashPercent, cashProfit, totalProfit;
    private ImageView imageView1, imageView2, imageView3;

    private ProgressBar progressBar;
    private Button btnEdit, btnDelete;
    private String id;
    private CollectionReference collRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_profile, container, false);

        collRef = FirebaseFirestore.getInstance().collection("Investors");
        progressBar = view.findViewById(R.id.detail_progress_bar);

        name = view.findViewById(R.id.detail_name);
        companyID = view.findViewById(R.id.detail_company_id);
        phone = view.findViewById(R.id.detail_phone);
        nrc = view.findViewById(R.id.detail_nrc);
        address = view.findViewById(R.id.detail_address);

        imageView1 = view.findViewById(R.id.detail_img_one);
        imageView2 = view.findViewById(R.id.detail_img_two);
        imageView3 = view.findViewById(R.id.detail_img_three);

        amount811 = view.findViewById(R.id.detail_plan_811_amount);
        percent811 = view.findViewById(R.id.detail_plan_811_percent);
        date811 = view.findViewById(R.id.detail_date_811);

        amount58 = view.findViewById(R.id.detail_plan_58_amount);
        percent58 = view.findViewById(R.id.detail_plan_58_percent);
        date58 = view.findViewById(R.id.detail_date_58);

        amount456 = view.findViewById(R.id.detail_plan_456_amount);
        percent456 = view.findViewById(R.id.detail_plan_456_percent);
        date456 = view.findViewById(R.id.detail_date_456);

        cashAmount = view.findViewById(R.id.cash_amount);
        cashPercent = view.findViewById(R.id.cash_percent);
        cashProfit = view.findViewById(R.id.cash_profit);
        totalProfit = view.findViewById(R.id.detail_total_profit);

        btnEdit = view.findViewById(R.id.detail_edit_btn);
        btnDelete = view.findViewById(R.id.detail_delete_btn);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra(EditProfileActivity.ID_PASS, id);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                DeleteSubCollection();
                DeleteCollection();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }, 3000);
            }
        });

        Bundle data = getArguments();
        bundlePass = data.getString(InvestorDetailActivity.BUNDLE_PASS);

        return view;
    }

    private void DeleteSubCollection() {
        FirebaseFirestore.getInstance().collection("Investors/"+id+"/First Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()){
                            FirebaseFirestore.getInstance().collection("Investors/"+id+"/First Date").document(qs.getId()).delete();
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Investors/"+id+"/Second Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()){
                            FirebaseFirestore.getInstance().collection("Investors/"+id+"/Second Date").document(qs.getId()).delete();
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("Investors/"+id+"/Final Date").get()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot qs : task.getResult()){
                            FirebaseFirestore.getInstance().collection("Investors/"+id+"/Final Date").document(qs.getId()).delete();
                        }
                    }
                });
    }

    private void DeleteCollection() {
        collRef.document(id).delete();
    }

    @Override
    public void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.document(bundlePass)
                .get().addOnSuccessListener(getActivity(),new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);

                id = documentSnapshot.getId();

                name.setText(documentSnapshot.getString("name"));
                companyID.setText(documentSnapshot.getString("companyID"));
                phone.setText(documentSnapshot.getString("phone"));
                nrc.setText(documentSnapshot.getString("nrc"));
                address.setText(documentSnapshot.getString("address"));
                amount811.setText(documentSnapshot.getString("amount811"));
                percent811.setText(documentSnapshot.getString("percent811"));
                date811.setText(documentSnapshot.getString("date811"));
                amount58.setText(documentSnapshot.getString("amount58"));
                percent58.setText(documentSnapshot.getString("percent58"));
                date58.setText(documentSnapshot.getString("date58"));
                amount456.setText(documentSnapshot.getString("amount456"));
                percent456.setText(documentSnapshot.getString("percent456"));
                date456.setText(documentSnapshot.getString("date456"));
                cashAmount.setText(documentSnapshot.getString("cashAmount"));
                cashPercent.setText(documentSnapshot.getString("cashPercent"));
                cashProfit.setText(documentSnapshot.getString("cashProfit"));
                totalProfit.setText(documentSnapshot.getString("preProfit"));

                if (documentSnapshot.getString("imgUrlOne").equals("")){
                    return;
                }else{
                    Picasso.get().load(documentSnapshot.getString("imgUrlOne")).into(imageView1);
                }

                if (documentSnapshot.getString("imgUrlTwo").equals("")){
                    return;
                }else{
                    Picasso.get().load(documentSnapshot.getString("imgUrlTwo")).into(imageView2);
                }

                if (documentSnapshot.getString("imgUrlThree").equals("")){
                    return;
                }else{
                    Picasso.get().load(documentSnapshot.getString("imgUrlThree")).into(imageView3);
                }
            }
        });
    }
}
