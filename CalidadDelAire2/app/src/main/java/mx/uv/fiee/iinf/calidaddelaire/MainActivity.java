package mx.uv.fiee.iinf.calidaddelaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import mx.uv.fiee.iinf.calidaddelaire.Models.ApiRoot;
import mx.uv.fiee.iinf.calidaddelaire.Models.Data;
import mx.uv.fiee.iinf.calidaddelaire.Models.Estacion;
import mx.uv.fiee.iinf.calidaddelaire.Models.Medida;
import mx.uv.fiee.iinf.calidaddelaire.Models.Resultado;
import mx.uv.fiee.iinf.calidaddelaire.Models.Ubicacion;
import mx.uv.fiee.iinf.calidaddelaire.Service.PollutionService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private RecyclerView rvPollution;
    private LinkedList<Data> listData;
    private static final String URL = "https://api.datos.gob.mx/v1/";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater (this);
        inflater.inflate (R.menu.main, menu);
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId () == R.id.mnuMaps) {
            Intent intent = new Intent (this, MapsActivity.class);
            intent.putExtra ("DATA", listData);
            startActivity (intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        rvPollution = findViewById (R.id.rvPollution);
        rvPollution.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        rvPollution.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor ();
        interceptor.level (HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor (interceptor).build ();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).client(client).addConverterFactory (GsonConverterFactory.create ()).build ();
        PollutionService service = retrofit.create (PollutionService.class);
        service.getPollutionMeasurements().enqueue (new Callback<ApiRoot> () {
            @Override
            public void onResponse (Call<ApiRoot> call, Response<ApiRoot> response) {
                if (response.body () == null) return;

                ArrayList<Resultado> resultados = response.body().getResultados ();
                listData = new LinkedList<> ();

                for (Resultado resultado: resultados) {
                    if (resultado.getEstaciones().size () > 0) {
                        Estacion estacion = resultado.getEstaciones().get (0);

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

                /*runOnUiThread (() -> */rvPollution.setAdapter (new PollutionAdapter (getBaseContext (), listData))/*)*/;
            }

            @Override
            public void onFailure(Call<ApiRoot> call, Throwable t) {
                runOnUiThread (() -> Toast.makeText (getBaseContext(), "Error al descargar el archivo", Toast.LENGTH_LONG).show ());
            }
        });

    }

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