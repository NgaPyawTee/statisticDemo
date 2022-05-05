package com.homework.statisticdemo.investor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.homework.statisticdemo.R;
import com.homework.statisticdemo.investor.detail.InvestorDetailActivity;
import com.homework.statisticdemo.model.Investor;

import java.util.ArrayList;
import java.util.List;

public class InvestorCategoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private FirebaseFirestore db;
    private CollectionReference collRef;
    private List<Investor> list;
    private List<String> nameList;
    private List<String> idList;
    private ProgressBar progressBar;
    private FloatingActionButton fBtn;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_category);

        toolbar = findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Investor");

        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Investors");

        progressBar = findViewById(R.id.investor_category_progress_bar);

        list = new ArrayList<>();
        nameList = new ArrayList<>();
        idList = new ArrayList<>();

        recyclerView = findViewById(R.id.investor_category_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(this, list);
        adapter.setListener(new CategoryAdapter.Listener() {
            @Override
            public void onClick(int position) {
               Intent intent = new Intent(InvestorCategoryActivity.this, InvestorDetailActivity.class);
               intent.putExtra(InvestorDetailActivity.ID_PASS, idList.get(position));
               startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        fBtn = findViewById(R.id.floating_btn);
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvestorCategoryActivity.this,AddInvestorActivity.class));
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.investor_category_menu_item,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setTextColor(Color.YELLOW);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.background_dark);

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                searchAutoComplete.setText(s);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (nameList.contains(query)){
                    int pos = nameList.indexOf(query);

                    Intent intent = new Intent(InvestorCategoryActivity.this, InvestorDetailActivity.class);
                    intent.putExtra(InvestorDetailActivity.ID_PASS, idList.get(pos));
                    startActivity(intent);
                }

               /* Intent intent =  new Intent(InvestorCategoryActivity.this, InvestorDetailActivity.class);
                intent.putExtra(InvestorDetailActivity.NAME_PASS,query);
                startActivity(intent); */
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetrieveData();
    }

    private void RetrieveData() {
        collRef.orderBy("name").get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                list.clear();
                idList.clear();
                nameList.clear();
                for (QueryDocumentSnapshot qs : queryDocumentSnapshots) {
                    Investor data = qs.toObject(Investor.class);
                    data.setId(qs.getId());
                    idList.add(data.getId());
                    list.add(data);
                    nameList.add(data.getName());
                }
                searchAutoComplete.setAdapter(arrayAdapter);
                adapter.notifyDataSetChanged();
            }
        });
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nameList);
    }
}
