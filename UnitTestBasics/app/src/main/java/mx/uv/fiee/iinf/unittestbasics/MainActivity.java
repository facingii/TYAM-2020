package mx.uv.fiee.iinf.unittestbasics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        final EditText editText = findViewById(R.id.inEmail);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            boolean isValid = Utils.checkEmailForValidity(editText.getText().toString());

            if (isValid) {
                Toast.makeText(getApplicationContext(), "Email is valid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Email not valid", Toast.LENGTH_LONG).show();
            }
        });
    }
}
