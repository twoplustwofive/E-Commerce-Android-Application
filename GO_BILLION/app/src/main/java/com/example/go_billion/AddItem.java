package com.example.go_billion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Result;

public class AddItem extends Fragment {
    private Button buttonAdd,add_img1,add_img2,add_img3;
    private EditText productName, Price, Category, Desc;
    private ImageView img1,img2,img3;
    public static ProductDBHelper productDBHelper;
    final int REQUEST_CODE_GALLERY = 999;
    public int imgnumber;
    public final int PICK_IMAGE_REQUEST = 71;
    public Spinner spinner;
    Uri filePath;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_additem, container, false);
        buttonAdd = root.findViewById(R.id.add_button);
        productName = root.findViewById(R.id.productName);
        Price = root.findViewById(R.id.price);
        Desc = root.findViewById(R.id.desc);
        spinner = root.findViewById(R.id.category);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.category));
        spinner.setAdapter(adapter);

        img1 = root.findViewById(R.id.img1);
        img2 = root.findViewById(R.id.img2);
        img3 = root.findViewById(R.id.img3);
        add_img1 = root.findViewById(R.id.add_img1);
        add_img2 = root.findViewById(R.id.add_img2);
        add_img3 = root.findViewById(R.id.add_img3);

        img1.setImageResource(R.drawable.download);
        img2.setImageResource(R.drawable.download);
        img3.setImageResource(R.drawable.download);

        productDBHelper = new ProductDBHelper(getContext(),"ProductDB.sqlite", null, 1);
        productDBHelper.queryData("CREATE TABLE IF NOT EXISTS PRODUCT (ID INTEGER PRIMARY KEY AUTOINCREMENT, productName VARCHAR, price VARCHAR, category VARCHAR, description VARCHAR, img1 BLOG, img2 BLOG, img3 BLOG)");

        add_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgnumber = 1;
                chooseImage();

            }
        });

        add_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgnumber = 2;
                chooseImage();
            }
        });

        add_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgnumber = 3;
                chooseImage();
            }
        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString().trim();
                String price = Price.getText().toString().trim();
                String desc = Desc.getText().toString().trim();
                String category = spinner.getSelectedItem().toString();
                Toast.makeText(getContext(),"JUST HERE",Toast.LENGTH_LONG).show();
                byte[] Img1 = imageViewToByte(img1);
                byte[] Img2 = imageViewToByte(img2);
                byte[] Img3 = imageViewToByte(img3);
                if(name.length()==0||price.length()==0||category.length()==0||desc.length()==0||Img1.length==0||Img2.length==0||Img3.length==0)
                {
                    Toast.makeText(getContext(),"Insufficient Information!",Toast.LENGTH_LONG).show();
                    return;
                }

                try {

                    productDBHelper.insertData(
                            productName.getText().toString(),
                            Price.getText().toString(),
                            spinner.getSelectedItem().toString(),
                            Desc.getText().toString(),
                            Img1,
                            Img2,
                            Img3
                    );

                    Toast.makeText(getContext(),"Product Added Succesfully", Toast.LENGTH_LONG).show();
                    productName.setText("");
                    Price.setText("");
                    Category.setText("");
                    Desc.setText("");
                    img1.setImageResource(R.drawable.download);
                    img2.setImageResource(R.drawable.download);
                    img3.setImageResource(R.drawable.download);
                    Toast.makeText(getContext(),"HERE",Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }

        });




        return root;
    }


    private byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {

                if(imgnumber==1)
                    Picasso.get().load(filePath).into(img1);

                if(imgnumber==2)
                    Picasso.get().load(filePath).into(img2);

                if(imgnumber==3)
                    Picasso.get().load(filePath).into(img3);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}










