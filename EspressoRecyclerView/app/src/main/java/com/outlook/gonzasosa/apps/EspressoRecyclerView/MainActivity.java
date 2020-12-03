package com.outlook.gonzasosa.apps.EspressoRecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        final TextView textView = findViewById (R.id.text);
        textView.setBackgroundColor (Color.LTGRAY);
        textView.setVisibility (View.GONE);

        RecyclerView recyclerView = findViewById (R.id.recycler_view);
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager(this));

        recyclerView.setAdapter (new NumberedAdapter (50, position -> {
            textView.setText (String.valueOf (position));
            textView.setVisibility (View.VISIBLE);
        }));
    }
}