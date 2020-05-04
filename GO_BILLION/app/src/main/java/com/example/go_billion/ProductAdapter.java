package com.example.go_billion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private Boolean flag;
    public ProductAdapter(Context context, Cursor cursor, Boolean flag){
        this.mContext = context;
        this.mCursor = cursor;
        this.flag = flag;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        public TextView ProductName;
        public TextView ProductPrice;
        public ImageView imageView;
        public CardView cardView;
        public Button wishlist;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName = itemView.findViewById(R.id.ProductName);
            ProductPrice = itemView.findViewById(R.id.Price);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.product_image);
            wishlist = itemView.findViewById(R.id.wishlist);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.product, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
        {
            return;
        }
        String productName = mCursor.getString(mCursor.getColumnIndex("productName"));
        int productPrice = mCursor.getInt(mCursor.getColumnIndex("price"));
        final Integer id = mCursor.getInt(mCursor.getColumnIndex("ID"));

        if(Wish_List.WishList.get(id)!=null)
            holder.wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);

        holder.ProductName.setText(productName);
        String price = String.valueOf(productPrice);
        price = "Rs."+price;
        holder.ProductPrice.setText(price);
        final byte [] buf = mCursor.getBlob(mCursor.getColumnIndex("img1"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(buf,0,buf.length);
        if(buf.length==0)
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        else
        holder.imageView.setImageBitmap(bitmap);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ProductDetails.class);
                intent.putExtra("ID",id.toString());
                v.getContext().startActivity(intent);
            }
        });

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Wish_List.WishList.get(id)==null)
                {
                    holder.wishlist.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    Wish_List.WishList.put(id,true);
                    Toast.makeText(mContext,"Product added to wish list.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    holder.wishlist.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    Wish_List.WishList.remove(id);
                    Toast.makeText(mContext,"Product removed from wish list.",Toast.LENGTH_LONG).show();
                    if(flag)
                    if(Wish_List.WishList.size()==0)
                    {
                        Toast.makeText(mContext,"WishList Became Empty!",Toast.LENGTH_LONG).show();
                        mContext.startActivity(new Intent(mContext,MainActivity.class));
                        ((MainActivity)mContext).finish();
                    }
                }

                if(flag)
                    wishlist();
            }
        });



    }

    @Override
    public int getItemCount() {
        if(mCursor==(null))
        {
            return 0;
        }
        return mCursor.getCount();
    }

     public void swapCursor(Cursor newCursor)
     {
         if(mCursor!=null)
             mCursor.close();
         mCursor = newCursor;
         if(newCursor!=null)
             notifyDataSetChanged();
     }

    public void filter(String category){
        ProductDBHelper dbHelper = new ProductDBHelper(mContext,"ProductDB.sqlite",null,1);
        SQLiteDatabase mDatabase = dbHelper.getReadableDatabase();
        if(category.equals("All"))
        {swapCursor(mDatabase.rawQuery("SELECT * FROM PRODUCT",null));return;}
        Cursor cursor =  mDatabase.rawQuery("SELECT * FROM PRODUCT WHERE category = ?", new String[]{category});
        swapCursor(cursor);
    }

    public void wishlist(){
        ProductDBHelper dbHelper = new ProductDBHelper(mContext,"ProductDB.sqlite",null,1);
        SQLiteDatabase mDatabase = dbHelper.getReadableDatabase();
        Cursor cursor =  mDatabase.rawQuery("SELECT * FROM PRODUCT", null);
        Cursor fcursor = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            if(Wish_List.WishList.size()>0 && Wish_List.WishList.get(id)!=null){
                MatrixCursor matrixCursor = new MatrixCursor(new String[] { "ID", "productName", "price", "category", "description", "img1", "img2", "img3"});
                String name = cursor.getString(cursor.getColumnIndex("productName"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] img1 = cursor.getBlob(cursor.getColumnIndex("img1"));
                byte[] img2 = cursor.getBlob(cursor.getColumnIndex("img2"));
                byte[] img3 = cursor.getBlob(cursor.getColumnIndex("img3"));
                matrixCursor.addRow(new Object[] { id, name, price, category, description, img1, img2, img3});

// Merge your existing cursor with the matrixCursor you created.
                MergeCursor mergeCursor = new MergeCursor(new Cursor[] { matrixCursor, fcursor});
                fcursor = mergeCursor;
            }
        }

        swapCursor(fcursor);
    }
}
