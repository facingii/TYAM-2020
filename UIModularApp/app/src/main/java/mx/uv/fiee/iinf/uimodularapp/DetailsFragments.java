package mx.uv.fiee.iinf.uimodularapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class DetailsFragments extends Fragment {
    DetailsFragments detailsFragments;

    DetailsFragments instance (Bundle args) {
        if (detailsFragments == null) {
            detailsFragments = new DetailsFragments ();
        }

        detailsFragments.setArguments (args);
        return detailsFragments;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_details, container, false);
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
