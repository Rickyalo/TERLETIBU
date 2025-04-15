package com.example.teletibu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 4 Columns

        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            items.add("Card " + i);
        }

        CardAdapter adapter = new CardAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}

