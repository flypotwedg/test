package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List names=new ArrayList<String>();
    List UID=new ArrayList<String>();

    TextView textView;

    //todo
    //get list of users names and uid
    //put info into list
    //display names, attach uid in backend
    //each item is a button to new activity
    //press item, pass uid
    //activity shows their info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView) findViewById(R.id.listView);
        textView=(TextView) findViewById(R.id.textView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,names);

        DatabaseReference database= FirebaseDatabase.getInstance().getReference();
        Query q=database.child("Users").orderByChild("fullName");

        Toast.makeText(this, "reference got, query created", Toast.LENGTH_SHORT).show();

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        names.add(child.child("fullName").getValue().toString());
                        UID.add(child.getKey());
                    }
                    textView.setText(UID.toString());

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                                long id) {
                            Intent intent=new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("UID", (String) UID.get(pos));
                            startActivity(intent);
                        }

                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"error retrieving data",Toast.LENGTH_LONG);
            }
        });


    
    }
}