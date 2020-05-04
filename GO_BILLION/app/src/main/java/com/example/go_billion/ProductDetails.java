package com.example.go_billion;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductDetails extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    private Button share;
    private TextView Price,Name,Des1,Des2,Des3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Bundle bundle = getIntent().getExtras();
        ProductDBHelper dbHelper = new ProductDBHelper(getApplicationContext(),"ProductDB.sqlite",null,1);
        SQLiteDatabase mDatabase = dbHelper.getReadableDatabase();
        final Cursor cursor =  mDatabase.rawQuery("SELECT * FROM PRODUCT WHERE ID = ?", new String[]{bundle.getString("ID")});
        ArrayList<byte[]> images = new ArrayList<>();
        String shareSub = new String();
        String shareBody = new String();
        String price = new String();
        String name = new String();
        while (cursor!=null && cursor.moveToNext())
        {images.add(cursor.getBlob(cursor.getColumnIndex("img1")));
        images.add(cursor.getBlob(cursor.getColumnIndex("img2")));
        images.add(cursor.getBlob(cursor.getColumnIndex("img3")));
        name = cursor.getString(cursor.getColumnIndex("productName"));
        shareBody = cursor.getString(cursor.getColumnIndex("description"));
        shareSub = cursor.getString(cursor.getColumnIndex("productName"))+ "   " + shareBody;
        price = cursor.getString(cursor.getColumnIndex("price"));
        }
        Price = findViewById(R.id.product_price);
        Name = findViewById(R.id.product_name);
        Des1 = findViewById(R.id.des1);
        Des2 = findViewById(R.id.des2);
        Des3 = findViewById(R.id.des3);
        price = "Rs."+ price;
        Price.setText(price);
        Name.setText(name);
        final String sSub = shareSub;
        final String sBody = shareBody;
        RecyclerView recyclerView = findViewById(R.id.horizontal_image_list);
        imageAdapter = new ImageAdapter(getApplicationContext(),images);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(imageAdapter);
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = (new Intent(Intent.ACTION_SEND));
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,sSub);
                i.putExtra(Intent.EXTRA_TEXT,sBody);
                startActivity(Intent.createChooser(i,"Share using"));
            }
        });

        String str = shareBody;
        String[] spl = str.split(",");
        ArrayList arrayList = new ArrayList(Arrays.asList(spl));
        if(arrayList.size()>0)
        {
            String s = (String) arrayList.get(0);
            s = "1. " + s;
            Des1.setText(s);
        }
        if(arrayList.size()>1)
        {
            String s = (String) arrayList.get(1);
            s = "2. "+s;
            Des2.setText(s);
        }
        if(arrayList.size()>2)
        {
            String s = (String) arrayList.get(2);
            s = "3. "+s;
            Des3.setText(s);
        }

    }
}
