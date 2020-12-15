package com.outlook.gonzasosa.apps.espressouitest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_second);

        TextView resultView = findViewById (R.id.resultView);
        Bundle data = getIntent ().getExtras ();
        if (data == null) return;

        String input = data.getString ("input");
        resultView.setText (input);
    }
}
