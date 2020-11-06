package mx.uv.fiee.iinf.uimodularapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity implements OnItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        getFragmentManager ()
                .beginTransaction()
                .add (R.id.mainContainer, new ListFragment ())
                .commit();
    }

    @Override
    public void itemSelected (String text, int resourceId) {
        Toast.makeText (getBaseContext(), text, Toast.LENGTH_LONG).show ();
    }
}
