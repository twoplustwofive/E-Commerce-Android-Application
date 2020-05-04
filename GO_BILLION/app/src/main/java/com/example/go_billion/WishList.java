package com.example.go_billion;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WishList extends Fragment {

    private SQLiteDatabase mDatabase;
    private ProductAdapter mAdapter;
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wishlist,container,false);

        ProductDBHelper dbHelper = new ProductDBHelper(getContext(),"ProductDB.sqlite",null,1);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = view.findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor cursor = getAllItems();
        mAdapter = new ProductAdapter(getContext(),cursor,true);
        mAdapter.wishlist();
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private Cursor getAllItems(){
        return mDatabase.query(
                "PRODUCT",
                null,
                null,
                null,
                null,
                null,
                "productName"
        );
    }

}