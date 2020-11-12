package mx.uv.fiee.iinf.calidaddelaire;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private RecyclerView rvPollution;
    private static final String URL = "https://api.datos.gob.mx/v1/calidadAire";

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        rvPollution = findViewById (R.id.rvPollution);
        rvPollution.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        rvPollution.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));

        OkHttpClient client = new OkHttpClient.Builder ().build ();
        Request request = new Request.Builder ().url (URL).build ();
        client.newCall (request).enqueue (new Callback () {
            @Override
            public void onFailure (@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse (@NotNull Call call, @NotNull Response response) throws IOException {
                String json = "";

                try {
                    json = response.body().string ();
                } catch (Exception ex) {
                    ex.printStackTrace ();
                    return;
                }

                parseJsonAndSetAdapter (json);
            }
        });
    }

    private void parseJsonAndSetAdapter (String json) {
        Gson gson = new Gson ();
        ApiRoot root = gson.fromJson (json, ApiRoot.class);
        ArrayList<Resultado> resultados = root.getResultados ();
        LinkedList<Data> listData = new LinkedList<> ();

        for (Resultado resultado: resultados) {
            if (resultado.getEstaciones().size () > 0) {
                Estacion estacion = resultado.getEstaciones().get(0);
                if (estacion.getMedidas().size () > 0) {
                    Medida medida = estacion.getMedidas().get (0);

                    Data data = new Data ();
                    data.name = estacion.getName ();
                    data.location = estacion.getUbicacion ();
                    data.time = medida.getTime ();
                    data.value = medida.getValue ();
                    data.unit = medida.getUnit ();
                    data.pollutant = medida.getPollutant ();

                    listData.add (data);
                }
            }
        }

        runOnUiThread (() -> rvPollution.setAdapter (new PollutionAdapter (getBaseContext (), listData)));
    }
}

class Data {
    String name;
    Ubicacion location;
    String time;
    String value;
    String unit;
    String pollutant;
}

class PollutionAdapter extends RecyclerView.Adapter<PollutionVH> {
    private Context context;
    private LinkedList<Data> data;

    public PollutionAdapter (Context context, LinkedList<Data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public PollutionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (context).inflate (R.layout.row, parent, false  );
        return new PollutionVH (view);
    }

    @Override
    public void onBindViewHolder (@NonNull PollutionVH holder, int position) {
        Data foo = data.get (position);
        holder.setData (foo.name, foo.time, foo.value + " " + foo.unit + " " + foo.pollutant);
    }

    @Override
    public int getItemCount() {
        return data.size ();
    }
}

class PollutionVH extends RecyclerView.ViewHolder {
    private TextView tvName, tvTime, tvValue;

    public PollutionVH(@NonNull View itemView) {
        super (itemView);
        tvName = itemView.findViewById (R.id.tvName);
        tvTime = itemView.findViewById (R.id.tvTime);
        tvValue = itemView.findViewById (R.id.tvValue);
    }

    public void setData (String name, String time, String value) {
        tvName.setText (name);
        tvTime.setText (time);
        tvValue.setText (value);
    }

}