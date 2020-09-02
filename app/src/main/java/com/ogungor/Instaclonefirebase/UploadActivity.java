package com.ogungor.Instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    Bitmap selectedImage;
    ImageView imageView;
    EditText postText;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    Uri dataImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        imageView = findViewById(R.id.imageView);
        postText = findViewById(R.id.postName);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGallery, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, 2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            dataImage = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), dataImage);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void upload(View view) {
        UUID uuid=UUID.randomUUID();
        final String imageName="images/"+uuid+".jpg";

        if (dataImage != null) {
            storageReference.child(imageName).putFile(dataImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   StorageReference newReference=storage.getInstance().getReference(imageName);
                   newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           String downloadUrl=uri.toString();
                           FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                           String userEmail=firebaseUser.getEmail();
                           String comment=postText.getText().toString();

                           HashMap<String,Object> hashMap=new HashMap<>();
                           hashMap.put("useremail",userEmail);
                           hashMap.put("downloadurl",downloadUrl);
                           hashMap.put("comment", comment);
                           hashMap.put("date", FieldValue.serverTimestamp());

                           firebaseFirestore.collection("Posts").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                               @Override
                               public void onSuccess(DocumentReference documentReference) {
                                   Intent feedIntent = new Intent(UploadActivity.this,FeedActivity.class);
                                   feedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   startActivity(feedIntent);
                               }
                           });

                       }
                   });


                    }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });

        }else
        {
            Toast.makeText(UploadActivity.this,"Not Selected",Toast.LENGTH_LONG).show();
        }
    }

}