package com.example.eban.modeltest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eban.modeltest.Interface.ItemClickListener;
import com.example.eban.modeltest.Model.Category;
import com.example.eban.modeltest.R;
import com.example.eban.modeltest.Start;
import com.example.eban.modeltest.ViewHolder.CategoryViewHolder;
import com.example.eban.modeltest.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    View myFragment;

    RecyclerView categoryList;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> mFirebaseRecyclerAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference categories;

    public static CategoryFragment newInstance() {
        CategoryFragment catragoryFragment = new CategoryFragment();
        return catragoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        categories = mDatabase.getReference("Category");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);
        categoryList = myFragment.findViewById(R.id.list_category);
        categoryList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(container.getContext());
        categoryList.setLayoutManager(mLayoutManager);

        loadCategories();

        return myFragment;
    }

    private void loadCategories() {
        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class,
                R.layout.category_layout,
                CategoryViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int position) {
                viewHolder.category_name.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.category_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getContext(), String.format("%s|%s", getRef(position).getKey(), model.getName()), Toast.LENGTH_SHORT).show();
                        Intent startGame = new Intent(getActivity(), Start.class);
                        Common.categoryId = mFirebaseRecyclerAdapter.getRef(position).getKey();
                        Common.categoryName=model.getName();
                        startActivity(startGame);
                    }
                });
            }
        };
        mFirebaseRecyclerAdapter.notifyDataSetChanged();
        categoryList.setAdapter(mFirebaseRecyclerAdapter);
    }
}
