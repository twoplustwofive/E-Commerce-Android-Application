package com.example.go_billion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Store extends Fragment {

    private SQLiteDatabase mDatabase;
    private ProductAdapter mAdapter;
    private Spinner spinner;
    private View viewroot;
    private ImageView imageView1,imageView2,imageView3;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewroot = inflater.inflate(R.layout.fragment_store,container,false);
        spinner = viewroot.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.category));
        spinner.setAdapter(adapter);



        ProductDBHelper dbHelper = new ProductDBHelper(getContext(),"ProductDB.sqlite",null,1);
        dbHelper.queryData("CREATE TABLE IF NOT EXISTS PRODUCT (ID INTEGER PRIMARY KEY AUTOINCREMENT, productName VARCHAR, price VARCHAR, category VARCHAR, description VARCHAR, img1 BLOG, img2 BLOG, img3 BLOG)");
        mDatabase = dbHelper.getReadableDatabase();

        RecyclerView recyclerView = viewroot.findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProductAdapter(getContext(),getAllItems(),false);
        recyclerView.setAdapter(mAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = spinner.getSelectedItem().toString();
                mAdapter.filter(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAdapter.filter("All");
            }
        });

        return viewroot;
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