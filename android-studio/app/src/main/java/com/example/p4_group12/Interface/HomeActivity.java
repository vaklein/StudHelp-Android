package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.DAO.Advertisement;
import com.example.p4_group12.DAO.Tag;
import com.example.p4_group12.Interface.adapter.AdvertisementListAdapter;
import com.example.p4_group12.Interface.adapter.CategoryListAdapter;
import com.example.p4_group12.R;
import com.example.p4_group12.database.API;

import java.util.ArrayList;

public class HomeActivity extends NavigationActivity{
    private CardView searchBarButton;
    private RecyclerView categoryRecyclerView;
    private RecyclerView.LayoutManager categoryLayoutManager;
    private CategoryListAdapter categoryListAdapter;
    private ArrayList<String> categoryList;
    private int notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);
        setTitleToolbar("Outil de recherche");

        searchBarButton = findViewById(R.id.search_bar_button);

        notif = (int) getIntent().getSerializableExtra("notif");
        if (notif == 1){
            int advertisment_id = (int) getIntent().getSerializableExtra("id");
            Log.v("jerem", "adv : "+advertisment_id);
            Advertisement add = API.getInstance().getAdvertisment(advertisment_id);
            Intent advertisementView = new Intent(getApplicationContext(), AdvertisementViewActivity.class);
            advertisementView.putExtra("ClickedAdvertisement", add);
            int i = 0;
            for (Tag tag : add.getTags()) {
                advertisementView.putExtra("tag"+i, tag);
                i++;
            }
            advertisementView.putExtra("Number of tags", i);
            startActivityForResult(advertisementView, 1);
        }

        searchBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                searchActivity.putExtra("ClickedCategory", "search all");
                startActivity(searchActivity);
            }
        });

        categoryList = GlobalVariables.getFaculties();

        categoryRecyclerView = findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setHasFixedSize(true);
        categoryLayoutManager = new GridLayoutManager(this, 3);

        categoryListAdapter = new CategoryListAdapter(categoryList);

        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
        categoryRecyclerView.setAdapter(categoryListAdapter);

        categoryListAdapter.setCategoryClickListener(new CategoryListAdapter.OnCategoryClickListener() {
            @Override
            public void OnCategoryClick(int position) {
                String faculty = categoryList.get(position);
                Log.v("Jules", "Clicked category is " + faculty);
                Log.v("Jules", "Category list size = " + categoryList.size());
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                searchActivity.putExtra("ClickedCategory", faculty);
                startActivityForResult(searchActivity, 1);
            }
        });
    }
}
