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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import mx.uv.fiee.iinf.tyam.swinfoapp.R;
import mx.uv.fiee.iinf.tyam.swinfoapp.interfaces.SWApiService;
import mx.uv.fiee.iinf.tyam.swinfoapp.models.People;
import mx.uv.fiee.iinf.tyam.swinfoapp.models.PeopleHeader;
import mx.uv.fiee.iinf.tyam.swinfoapp.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleFragment extends Fragment {

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

        service.getPeople().enqueue (new Callback<PeopleHeader>() {
            @Override
            public void onResponse(@NonNull Call<PeopleHeader> call, @NonNull Response<PeopleHeader> response) {
                if (response.body () == null) return;

                List<People> results = response.body().results;
                recyclerView.setAdapter (new PeopleAdapter (results));
            }

            @Override
            public void onFailure(@NonNull Call<PeopleHeader> call, @NonNull Throwable t) {
                Toast.makeText (view.getContext (), t.getMessage (), Toast.LENGTH_LONG).show ();
            }
        });
    }

}

class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private final List<People> data;

    public PeopleAdapter (List<People> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_item, parent, false);
        return new PeopleViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull PeopleViewHolder holder, int position) {
        People p = data.get (position);
        holder.tvData1.setText (p.name);
        holder.tvData2.setText (String.format (Locale.US, Utils.DATA_FORMAT , Utils.DATA_HEIGHT, p.height));
        holder.tvData3.setText (String.format (Locale.US, Utils.DATA_FORMAT, Utils.DATA_BIRTHYEAR, p.birthyear));
    }

    @Override
    public int getItemCount () {
        return data.size ();
    }

    static class PeopleViewHolder extends RecyclerView.ViewHolder {
        TextView tvData1;
        TextView tvData2;
        TextView tvData3;

        public PeopleViewHolder(@NonNull View itemView) {
            super (itemView);
            tvData1 = itemView.findViewById (R.id.tvData1);
            tvData2 = itemView.findViewById (R.id.tvData2);
            tvData3 = itemView.findViewById (R.id.tvData3);
        }
    }

}
