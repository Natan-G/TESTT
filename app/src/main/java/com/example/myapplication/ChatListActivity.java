package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    ListView userListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> users = new ArrayList<>();
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Button signOut;
    ArrayList<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userListView = findViewById(R.id.userListView);
        signOut = findViewById(R.id.signOut);

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(!dataSnapshot.child("email").getValue().toString().equals(mAuth.getCurrentUser().getEmail())){
                            if(dataSnapshot.child("email").getValue().toString().equals(adapterClass.email)){
                               Toast.makeText(ChatListActivity.this ,adapterClass.email , Toast.LENGTH_SHORT ).show();
                               users.add(dataSnapshot.child("email").getValue().toString());
                           }
                        }
                    }
                    arrayAdapter = new ArrayAdapter(ChatListActivity.this , android.R.layout.simple_list_item_1 , users);
                    userListView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatListActivity.this , "Failed to load user !" , Toast.LENGTH_SHORT).show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ChatListActivity.this , logInPage.class);
                startActivity(intent);
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChatListActivity.this , ChatActivity.class);
                intent.putExtra("email" , users.get(position));
                startActivity(intent);
            }
        });
    }
}