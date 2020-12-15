package mx.uv.fiee.iinf.animviewapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        EditText edtFahrentheit = findViewById (R.id.edtFahrenheit);
        EditText edtCelsius = findViewById (R.id.edtCelsius);

        Button btnJumping = findViewById (R.id.btnJumping);
        SpringAnimation springAnimation = new SpringAnimation (btnJumping, DynamicAnimation.Y);

        SpringForce springForce = new SpringForce ();
        springForce.setFinalPosition (btnJumping.getScaleY () );
        springForce.setDampingRatio (SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springForce.setStiffness (SpringForce.STIFFNESS_LOW);

        springAnimation.setSpring (springForce);

        btnJumping.setOnClickListener (view -> {
            springAnimation.setStartValue (2000f);
            springAnimation.start ();
        });

        Animation moveView = AnimationUtils.loadAnimation (getBaseContext (), R.anim.move);
        moveView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                double foo = Double.parseDouble (edtFahrentheit.getText ().toString ()) * 100;
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
