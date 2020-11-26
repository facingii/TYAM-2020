package mx.uv.fiee.iinfo.lastfmtracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import mx.uv.fiee.iinf.tyam.API.Fragments.TopArtistsFragment;
import mx.uv.fiee.iinf.tyam.API.Fragments.TopTracksFragment;
import mx.uv.fiee.iinf.tyam.R;

/**
 * Actividad principal y punto de entrada de la aplicación.
 * Su única funcionalidad es cargar al componente de fichas.
 * El componente que brinda la funcionalidad de fichas se encargará
 * de dibujar las vistas correspodientes.
 */
public class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        // obtiene la referencia al viewpager que representa la estructura de fichas
        ViewPager viewPager = findViewById (R.id.vpLastFM);
        FragmentPagerAdapter vpAdapter = new MyPagerAdapter (getSupportFragmentManager ());
        viewPager.setAdapter (vpAdapter); // asigna su adaptador
    }
}

/**
 * Para la vista en forma de fichas, se necesita un adaptador que construya la vista
 * corresponciente a cada vista.
 */
class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter (FragmentManager fm) {
        super (fm);
    }

    // de acuerdo al indice de cada ficha, se crea la vista correspondiente
    @NonNull
    @Override
    public Fragment getItem (int i) {
        switch (i) {
            case 0:
                return new TopArtistsFragment ();
            case 1:
                return new TopTracksFragment ();
            default:
                return null;
        }

    }

    @Override
    public int getCount () {
        return 2; // devuelve el número de fichas disponibles
    }

    // establece el título de cada ficha
    @Nullable
    @Override
    public CharSequence getPageTitle (int position) {
        switch (position) {
            case 0:
                return "Top Artists";
            case 1:
                return "Top Tracks";
            default:
                return "";
        }
    }
}