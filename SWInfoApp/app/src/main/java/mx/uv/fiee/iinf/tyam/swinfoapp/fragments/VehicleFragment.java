package mx.uv.fiee.iinf.tyam.swinfoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import mx.uv.fiee.iinf.tyam.swinfoapp.R;
import mx.uv.fiee.iinf.tyam.swinfoapp.interfaces.SWApiService;
import mx.uv.fiee.iinf.tyam.swinfoapp.models.Vehicle;
import mx.uv.fiee.iinf.tyam.swinfoapp.models.VehicleHeader;
import mx.uv.fiee.iinf.tyam.swinfoapp.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate (R.layout.fragments_ui, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById (R.id.rvElements);
        recyclerView.addItemDecoration (new DividerItemDecoration (view.getContext (), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator (new DefaultItemAnimator ());
        recyclerView.setLayoutManager (new LinearLayoutManager (view.getContext ()));

        SWApiService service = Utils.createService ();

        service.getVehicles().enqueue (new Callback<VehicleHeader>() {
            @Override
            public void onResponse(@NonNull Call<VehicleHeader> call, @NonNull Response<VehicleHeader> response) {
                if (response.body () == null) return;

                List<Vehicle> results = response.body().results;
                recyclerView.setAdapter (new VehicleAdapter (results));
            }

            @Override
            public void onFailure(@NonNull Call<VehicleHeader> call, @NonNull Throwable t) {
                Toast.makeText (view.getContext (), t.getMessage (), Toast.LENGTH_LONG).show ();
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity ();
        if (activity == null) return;

    }
}

class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<Vehicle> data;

    public VehicleAdapter (List<Vehicle> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_item, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = data.get (position);
        holder.tvData1.setText (vehicle.name);
        holder.tvData2.setText (String.format (Locale.US, Utils.DATA_FORMAT, "Model: ", vehicle.model));
        holder.tvData3.setText (String.format (Locale.US, Utils.DATA_FORMAT, "Manufacturer: ", vehicle.manufacturer));
    }

    @Override
    public int getItemCount() {
        return data.size ();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView tvData1;
        TextView tvData2;
        TextView tvData3;

        public VehicleViewHolder(@NonNull View itemView) {
            super (itemView);
            tvData1 = itemView.findViewById (R.id.tvData1);
            tvData2 = itemView.findViewById (R.id.tvData2);
            tvData3 = itemView.findViewById (R.id.tvData3);
        }
    }
}
