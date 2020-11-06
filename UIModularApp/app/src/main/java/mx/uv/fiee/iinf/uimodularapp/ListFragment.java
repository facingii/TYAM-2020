package mx.uv.fiee.iinf.uimodularapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListFragment extends Fragment {
    private OnPlanetSelectedListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);

        if (!(context instanceof OnPlanetSelectedListener)) {
            throw new ClassCastException ("Activity must implement OnPlanetSelectedListener interface");
        } else {
            listener = (OnPlanetSelectedListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity ();
        if (activity == null) return;

        RecyclerView rvList = activity.findViewById (R.id.rvList);
        rvList.setLayoutManager (new LinearLayoutManager (activity));
        rvList.addItemDecoration (new DividerItemDecoration (activity, DividerItemDecoration.VERTICAL));
        rvList.setItemAnimator (new DefaultItemAnimator ());

        DummyAdapter adapter = new DummyAdapter (activity);
        adapter.setOnPlanetSelectedListener (listener);

        rvList.setAdapter (adapter);
    }

}

class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.DummyViewHolder>
{
    private final String [] planets = { "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Neptune", "Pluto" };
    private final int [] planetPics = { R.drawable.mercury, R.drawable.venus, R.drawable.earth, R.drawable.mars,
            R.drawable.jupiter, R.drawable.saturn, R.drawable.uranus, R.drawable.neptune, R.drawable.pluto };

    private final Context context;
    private OnPlanetSelectedListener listener;

    public DummyAdapter (Context context) {
        this.context = context;
    }

    void setOnPlanetSelectedListener (OnPlanetSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DummyAdapter.DummyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (context).inflate (R.layout.list_item, parent, false);
        return new DummyViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull DummyAdapter.DummyViewHolder holder, int position) {
        String planet = planets [position];
        int planetPic = planetPics [position];

        holder.imageView.setImageResource (planetPic);
        holder.textView.setText (planet);

        holder.itemView.setOnClickListener (view -> {
            if (this.listener != null) {
                this.listener.itemSelected(planet, planetPic);
            }
        });
    }

    @Override
    public int getItemCount () {
        return planets.length;
    }

    class DummyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        public DummyViewHolder (@NonNull View itemView) {
            super (itemView);
            imageView = itemView.findViewById (R.id.ivPlanetPic);
            textView = itemView.findViewById (R.id.tvListItem);
        }
    }
}

interface OnPlanetSelectedListener {
    void itemSelected (String text, int resourceId);
}