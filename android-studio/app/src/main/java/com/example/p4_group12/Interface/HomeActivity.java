package com.example.p4_group12.Interface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.p4_group12.R;

public class HomeActivity extends NavigationActivity{
    private Button searchBarButton;

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
                Intent intentSearchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intentSearchActivity);
                finish();
            }
        });
    }
}
