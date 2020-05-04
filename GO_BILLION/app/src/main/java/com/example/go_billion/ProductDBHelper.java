package com.example.go_billion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ProductDBHelper extends SQLiteOpenHelper {

    public Context context;
    public ProductDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String productName,String price,String category,String description,byte[] img1,byte[] img2,byte[] img3){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PRODUCT VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,productName);
        statement.bindString(2,price);
        statement.bindString(3,category);
        statement.bindString(4,description);
        statement.bindBlob(5,img1);
        statement.bindBlob(6,img2);
        statement.bindBlob(7,img3);
        statement.executeInsert();
    }

    public Cursor getData(String sql){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addItem(String name,String price,String category,String description,String img1,String img2,String img3, ProductDBHelper dbHelper)
    {
        File file1 = new File(img1);
        File file2 = new File(img2);
        File file3 = new File(img3);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.child1);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byte1 = stream.toByteArray();

//        bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
//        stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byte2 = stream.toByteArray();
//
//        bitmap = BitmapFactory.decodeFile(file3.getAbsolutePath());
//        stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byte3 = stream.toByteArray();

        try {

            dbHelper.insertData(
                    name,
                    price,
                    category,
                    description,
                    byte1,
                    byte1,
                    byte1
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
