package com.example.studymateriallocator;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewMaterialsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Material> list;
    MaterialAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_materials);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new MaterialAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadData();
    }

    private void loadData() {
        db.collection("materials").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    list.clear();
                    queryDocumentSnapshots.forEach(doc -> {
                        Material m = new Material(
                                doc.getId(),
                                doc.getString("title"),
                                doc.getString("url"),
                                doc.getString("uploadedBy"),
                                doc.getLong("downloads") != null ? doc.getLong("downloads") : 0
                        );
                        list.add(m);
                    });
                    adapter.notifyDataSetChanged();
                });
    }
}