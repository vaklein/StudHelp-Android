package com.example.p4_group12.Interface.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p4_group12.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder> {

    public ArrayList<String> categoryList;
    private OnCategoryClickListener categoryClickListener;

    public interface OnCategoryClickListener {
        void OnCategoryClick(int position);
    }

    public void setCategoryClickListener(OnCategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }

    public static class CategoryListViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        public CategoryListViewHolder(@NonNull View itemView, OnCategoryClickListener categoryClickListener) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoryClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            categoryClickListener.OnCategoryClick(position);
                        }
                    }
                }
            });
        }
    }

    public CategoryListAdapter(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        CategoryListAdapter.CategoryListViewHolder clh = new CategoryListAdapter.CategoryListViewHolder(v, this.categoryClickListener);
        return  clh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.CategoryListViewHolder holder, int position) {
        String currentCategory = categoryList.get(position);
        holder.categoryName.setText(currentCategory);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
