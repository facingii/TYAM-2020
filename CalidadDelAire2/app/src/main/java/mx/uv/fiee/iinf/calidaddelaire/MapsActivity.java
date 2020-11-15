package mx.uv.fiee.iinf.calidaddelaire;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;

import mx.uv.fiee.iinf.calidaddelaire.Models.Data;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private ArrayList<Data> listdata;
    private GoogleMap map;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_maps);

        Intent intent = getIntent ();
        if (intent == null) return;

        Bundle bundle = intent.getExtras ();
        if (bundle == null) return;

        listdata = (ArrayList<Data>) bundle.getSerializable ("DATA");

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById (R.id.map);
        if (fragment == null) return;

        fragment.getMapAsync (this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        for (Data d: listdata) {
            double lat = Double.parseDouble (d.location.getLat ());
            double lon = Double.parseDouble (d.location.getLon ());

            LatLng foo = new LatLng (lat, lon);
            map.addMarker (new MarkerOptions ().position (foo).title (d.name));
            //map.moveCamera (CameraUpdateFactory.newLatLng (foo));
        }
    }
}
