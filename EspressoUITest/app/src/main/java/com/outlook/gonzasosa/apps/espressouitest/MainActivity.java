package com.outlook.gonzasosa.apps.espressouitest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        editText = findViewById (R.id.inputField);
    }

    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.changeText:
                editText.setText ("Leia Princess");
                break;
            case R.id.switchActivity:
                Intent intent = new Intent (this, SecondActivity.class);
                intent.putExtra ("input", editText.getText ().toString ());
                startActivity (intent);
                break;
        }
    }
}
