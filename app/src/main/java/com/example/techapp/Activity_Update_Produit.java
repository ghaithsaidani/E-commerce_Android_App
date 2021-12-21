package com.example.techapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Activity_Update_Produit extends AppCompatActivity {
    TextInputEditText brand, name, price,description;
    Button edit;
    ImageView image;
    Uri imageurl;
    DatabaseReference ref,ref1;
    StorageReference storageReference;
    ProgressBar progress;
    AutoCompleteTextView edittype;
    ArrayAdapter<String> adapterItems;
    String producttype;
    String[] items={"Wearable","Laptop","Phone","Drone"};
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageurl=data.getData();
            image.setImageURI(imageurl);
        }
    }
    private String getfileextension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produit);
        brand = findViewById(R.id.EditBrand);
        name = findViewById(R.id.EditName);
        price = findViewById(R.id.EditPrice);
        image = findViewById(R.id.EditImage);
        progress=findViewById(R.id.EditProgressBar);
        progress.setVisibility(View.GONE);
        edittype=findViewById(R.id.EditType);
        description=findViewById(R.id.EditDescription);
        String image1 = getIntent().getExtras().getString("image");
        Picasso.get().load(image1).into(image);
        edit = findViewById(R.id.edit);
        String key = getIntent().getStringExtra("key");

        String name1 = getIntent().getStringExtra("name");
        String brand1 = getIntent().getStringExtra("brand");
        String type1 = getIntent().getStringExtra("type");
        int price1 = getIntent().getIntExtra("price",0);
        String description1 = getIntent().getStringExtra("description");


        name.setText(name1);
        brand.setText(brand1);
        price.setText(String.valueOf(price1));
        description.setText(description1);

        adapterItems=new ArrayAdapter<String>(this,R.layout.list_item,items);
        edittype.setText(type1);

        edittype.setAdapter(adapterItems);



        edittype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                producttype=parent.getItemAtPosition(position).toString();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,2);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(type1) {
                    case "Wearable":
                        ref1= FirebaseDatabase.getInstance().getReference("Products").child("Wearable").child(name1 + " " + brand1);
                        break;
                    case "Laptop":
                        ref1 = FirebaseDatabase.getInstance().getReference("Products").child("Laptops").child(name1 + " " + brand1);
                        break;
                    case "Drone":
                        ref1 = FirebaseDatabase.getInstance().getReference("Products").child("Drones").child(name1 + " " + brand1);
                        break;
                    case "Phone":
                        ref1= FirebaseDatabase.getInstance().getReference("Products").child("Phones").child(name1 + " " + brand1);
                        break;
                }

                switch(edittype.getText().toString()) {
                    case "Wearable":
                        storageReference = FirebaseStorage.getInstance().getReference("products").child("wearable");
                        ref= FirebaseDatabase.getInstance().getReference("Products").child("Wearable").child(name1 + " " + brand1);
                        break;
                    case "Laptop":
                        storageReference = FirebaseStorage.getInstance().getReference("products").child("laptops");
                        ref = FirebaseDatabase.getInstance().getReference("Products").child("Laptops").child(name1 + " " + brand1);
                        break;
                    case "Drone":
                        storageReference = FirebaseStorage.getInstance().getReference("products").child("drones");
                        ref = FirebaseDatabase.getInstance().getReference("Products").child("Drones").child(name1 + " " + brand1);
                        break;
                    case "Phone":
                        storageReference = FirebaseStorage.getInstance().getReference("products").child("phones");
                        ref= FirebaseDatabase.getInstance().getReference("Products").child("Phones").child(name1 + " " + brand1);
                        break;
                }


                StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getfileextension(imageurl));
                filereference.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess (UploadTask.TaskSnapshot taskSnapshot){
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            ref.child("name").setValue(name.getText().toString());
                            ref.child("brand").setValue(brand.getText().toString());
                            ref.child("price").setValue(Integer.parseInt(price.getText().toString()));
                            ref.child("imageurl").setValue(uri.toString());
                            ref.child("description").setValue(description.getText().toString());
                            ref.child("type").setValue(description.getText().toString());

                        }
                    });



            }
        });


    }

});
}

}



