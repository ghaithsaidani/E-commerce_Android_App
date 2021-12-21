package com.example.techapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.techapp.modeles.Produit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddproductActivity extends AppCompatActivity {

    ImageView addimage;
    Uri imageurl;
    TextInputEditText productname,productbrand,productprice,productdescription;
    Button addproduct;
    DatabaseReference reference;
    StorageReference storageReference;
    ProgressBar progress;
    AutoCompleteTextView addproducttype;
    ArrayAdapter<String> adapterItems;
    String producttype;
    String[] items={"Wearable","Laptop","Phone","Drone"};


    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(.\\d+)?");
    }

    private String getfileextension(Uri uri){

        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_addproduct);
        addimage=findViewById(R.id.addproductimage);
        addproducttype=findViewById(R.id.addproducttype);
        productdescription=findViewById(R.id.addproductdescriptionedittext);
        productbrand=findViewById(R.id.addproductbrandedittext);
        productname=findViewById(R.id.addproductnameedittext);
        productprice=findViewById(R.id.addproductpriceedittext);
        addproduct=findViewById(R.id.addproduct);
        progress=findViewById(R.id.addproductprogressbar);
        progress.setVisibility(View.GONE);

        adapterItems=new ArrayAdapter<String>(this,R.layout.list_item,items);

        addproducttype.setAdapter(adapterItems);

        addproducttype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                producttype=parent.getItemAtPosition(position).toString();
            }
        });







        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,2);
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageurl != null) {

                    String productname1 = productname.getText().toString().trim();
                    String productprice1 = productprice.getText().toString().trim();
                    String productbrand1 = productbrand.getText().toString().trim();
                    String productdescription1 = productdescription.getText().toString().trim();

                    if (addproducttype.getText().toString().isEmpty()) {
                        addproducttype.setError("Type Required");
                        addproducttype.requestFocus();
                        return;
                    }
                    switch(producttype) {
                        case "Wearable":
                            storageReference = FirebaseStorage.getInstance().getReference("products").child("wearable");
                            reference = FirebaseDatabase.getInstance().getReference("Products").child("Wearable").child(productname1 + " " + productbrand1);
                            break;
                        case "Laptop":
                            storageReference = FirebaseStorage.getInstance().getReference("products").child("laptops");
                            reference = FirebaseDatabase.getInstance().getReference("Products").child("Laptops").child(productname1 + " " + productbrand1);
                            break;
                        case "Drone":
                            storageReference = FirebaseStorage.getInstance().getReference("products").child("drones");
                            reference = FirebaseDatabase.getInstance().getReference("Products").child("Drones").child(productname1 + " " + productbrand1);
                            break;
                        case "Phone":
                            storageReference = FirebaseStorage.getInstance().getReference("products").child("phones");
                            reference = FirebaseDatabase.getInstance().getReference("Products").child("Phones").child(productname1 + " " + productbrand1);
                            break;
                    }


                    if (productname1.isEmpty()) {
                        productname.setError("Name Required");
                        productname.requestFocus();
                        return;
                    }
                    if (productbrand1.isEmpty()) {
                        productbrand.setError("Brand Required");
                        productbrand.requestFocus();
                        return;
                    }

                    if (productdescription1.isEmpty()) {
                        productdescription.setError("Brand Required");
                        productdescription.requestFocus();
                        return;
                    }
                    if (productprice1.isEmpty()) {
                        productprice.setError("Price Required");
                        productprice.requestFocus();
                        return;
                    }
                    if (productdescription1.isEmpty()) {
                        productdescription.setError("Description Required");
                        productdescription.requestFocus();
                        return;
                    }
                     else {
                        progress.setVisibility(View.VISIBLE);
                        StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getfileextension(imageurl));
                        filereference.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Produit p=new Produit(uri.toString(),productname1,productbrand1,productdescription1,Integer.parseInt(productprice1),producttype);
                                        reference.child("name").setValue(p.getName());
                                        reference.child("brand").setValue(p.getBrand());
                                        reference.child("imageurl").setValue(p.getImageurl());
                                        reference.child("description").setValue(p.getDescription());
                                        reference.child("type").setValue(p.getType());
                                        reference.child("price").setValue(p.getPrice());
                                        progress.setVisibility(View.GONE);
                                        Intent i=new Intent(AddproductActivity.this,MainActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        });


                    }
                }

                else
                    Toast.makeText(AddproductActivity.this, "Please select a photo", Toast.LENGTH_SHORT).show();
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageurl=data.getData();
            addimage.setImageURI(imageurl);
        }
    }
}