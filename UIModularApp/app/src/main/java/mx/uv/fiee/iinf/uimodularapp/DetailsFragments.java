package mx.uv.fiee.iinf.uimodularapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DetailsFragments extends Fragment {
    public static final String TEXT_ID = "text";
    public static final String RESOURCE_ID = "resource";

    public static DetailsFragments instance (String text, int resourceID) {
        DetailsFragments  detailsFragments = new DetailsFragments ();

        Bundle bundle = new Bundle ();
        bundle.putString (TEXT_ID, text);
        bundle.putInt (RESOURCE_ID, resourceID);

        detailsFragments.setArguments (bundle);
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

        Activity activity = getActivity ();
        if (activity == null) return;

        Bundle bundle = getArguments ();
        if (bundle == null) return;

        String text = bundle.getString (TEXT_ID);
        TextView tvDetailsText = activity.findViewById (R.id.tcDetailsText);
        tvDetailsText.setText (text);
    }
}
