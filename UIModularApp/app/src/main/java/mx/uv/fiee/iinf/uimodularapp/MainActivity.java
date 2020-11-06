package mx.uv.fiee.iinf.uimodularapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity implements OnPlanetSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        getFragmentManager ()
                .beginTransaction()
                .add (R.id.mainContainer, new ListFragment ())
                .setTransition (FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void itemSelected (String text, int resourceId) {
        DetailsFragments detailsFragments = DetailsFragments.instance (text, resourceId);

        FragmentManager manager =  getFragmentManager ();
        FragmentTransaction transaction =  manager.beginTransaction ();

        View detailsContainer = findViewById (R.id.detailsContainer);
        if (detailsContainer == null) {
            transaction.add (R.id.mainContainer, detailsFragments);
        } else {
            transaction.add (R.id.detailsContainer, detailsFragments);
        }

        transaction.setTransition (FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit ();
    }
}
