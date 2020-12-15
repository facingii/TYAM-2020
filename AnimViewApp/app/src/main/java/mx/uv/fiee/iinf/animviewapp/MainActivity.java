package mx.uv.fiee.iinf.animviewapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        EditText edtFahrentheit = findViewById (R.id.edtFahrenheit);
        EditText edtCelsius = findViewById (R.id.edtCelsius);

        Animation moveView = AnimationUtils.loadAnimation (getBaseContext (), R.anim.move);
        moveView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                double foo = Double.valueOf (edtFahrentheit.getText ().toString ()) * 100;
                edtCelsius.setText (String.valueOf (foo));
            }

            @Override
            public void onAnimationRepeat (Animation animation) {}
        });

        Button btnConvert = findViewById (R.id.btnConvert);
        btnConvert.setOnClickListener (view -> {
            edtFahrentheit.startAnimation (moveView);
        });

    }
}
