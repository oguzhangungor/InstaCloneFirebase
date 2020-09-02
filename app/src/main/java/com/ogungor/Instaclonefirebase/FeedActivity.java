package com.ogungor.Instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RecyclerAdapter recyclerAdapter;
    ArrayList<String> userEmailList;
    ArrayList<String> userCommentList;
    ArrayList<String> userImagetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        userCommentList=new ArrayList<>();
        userEmailList=new ArrayList<>();
        userImagetList=new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFireston();
        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter= new RecyclerAdapter(userEmailList,userCommentList,userImagetList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getDataFromFireston() {

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> posts = documentSnapshot.getData();
                        String userMail = (String) posts.get("useremail");
                        String comment = (String) posts.get("comment");
                        String downloadUrl = (String) posts.get("downloadurl");
                        userEmailList.add(userMail);
                        userCommentList.add(comment);
                        userImagetList.add(downloadUrl);

                        recyclerAdapter.notifyDataSetChanged();


                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insta_option_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_post) {
            Intent intent = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.signout) {
            firebaseAuth.signOut();
            Intent intentSignOut = new Intent(FeedActivity.this, SingUpActivity.class);
            startActivity(intentSignOut);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}