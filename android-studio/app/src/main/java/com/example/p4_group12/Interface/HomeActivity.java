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
    private CardView goToFavouritesButton;
    private RecyclerView categoryRecyclerView;
    private RecyclerView.LayoutManager categoryLayoutManager;
    private CategoryListAdapter categoryListAdapter;
    private ArrayList<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);
        setTitleToolbar("Outil de recherche");

        searchBarButton = findViewById(R.id.search_bar_button);


        searchBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                searchActivity.putExtra("ClickedCategory", "search all");
                searchActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(searchActivity);
            }
        });

        goToFavouritesButton = findViewById(R.id.go_to_favourites_button);

        goToFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                searchActivity.putExtra("ClickedCategory", "favourites courses");
                searchActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                searchActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(searchActivity, 1);
            }
        });
    }
}
