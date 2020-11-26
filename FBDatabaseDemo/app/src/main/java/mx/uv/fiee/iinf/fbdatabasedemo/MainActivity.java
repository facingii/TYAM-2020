package mx.uv.fiee.iinf.fbdatabasedemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        Button btnSave = findViewById (R.id.btnSave);
        btnSave.setOnClickListener (view -> {
            saveNewSong ();
        });

        RecyclerView recyclerView = findViewById (R.id.rvList);
        recyclerView.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator (new DefaultItemAnimator ());
        recyclerView.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));

        Vector<Song> vSongs = new Vector<>();

        //auth = FirebaseAuth.getInstance ();
        //iniciarSesion ();

        FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference songs = database.getReference ("songs");

        // el evento SingleValueEvent itera por todos los elemento de la roma donde se
        // invoca, y cada subelemento es referenciado mediante el objeto DataSnapShot
        songs.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot) {
                // datasnapShot hace referencia a la rama SONGS
                // el método getChildren devuelve cada subelemento TRACK
                for (DataSnapshot item: dataSnapshot.getChildren ()) {
                    // getValue devuelve referencia al objeto contenido en cada TRACK
                    // y lo convierte al tipo de datos indicado en el parámetro
                    Song s = item.getValue (Song.class);
                    vSongs.add (s);
                    Log.d ("TAG", s.title + " " + s.year);
                }

                recyclerView.setAdapter (new MyAdater (vSongs));
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError) {
                Log.e ("TYAM", databaseError.getMessage ());
            }
        });
    }

    void iniciarSesion () {
        auth.signInAnonymously ()
                .addOnFailureListener(e -> Log.e ("TYAM", "Fail on anonymous auth", e));
    }

    /**
     * agrega un nuevo elemento a la rama songs
     */
    private void saveNewSong () {
        Song song = new Song ();
        song.title = "Cumbia del Coronavirus";
        song.author = "Varios";
        song.composer = "Varios";
        song.album = "Pandamia";
        song.year = 2020;
        song.company = "Company";
        song.cover = "Cubierta";

        FirebaseDatabase database = FirebaseDatabase.getInstance ();
        DatabaseReference songs = database.getReference ("songs");

        HashMap<String, Object> node = new HashMap<>();
        node.put ("Track20", song);

        songs.updateChildren (node)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText (getBaseContext (), "Ndde added successfully", Toast.LENGTH_LONG).show ();
                })
                .addOnFailureListener(e -> Toast.makeText (getBaseContext (), "Ndde add failed: " + e.getMessage (), Toast.LENGTH_LONG).show ());
    }
}

class Song {
    public String album;
    public String author;
    public String company;
    public String composer;
    public String cover;
    public String title;
    public int year;
}

class MyAdater extends RecyclerView.Adapter<MyAdater.MyViewHolder> {
    Vector<Song> data;

    public MyAdater (Vector<Song> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyAdater.MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdater.MyViewHolder holder, int position) {
        Song song = data.get (position);

        holder.tvTitle.setText (song.title);
        holder.tvComposer.setText (song.composer);
        holder.tvAlbum.setText (song.album);
        holder.tvYear.setText (song.year);
        holder.tvCompany.setText (song.company);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvComposer, tvAlbum, tvYear, tvCompany;

        public MyViewHolder(@NonNull View itemView) {
            super (itemView);

            tvTitle = itemView.findViewById (R.id.tvTitle);
            tvComposer = itemView.findViewById (R.id.tvComponser);
            tvAlbum = itemView.findViewById (R.id.tvAlbum);
            tvYear = itemView.findViewById (R.id.tvYear);
            tvCompany = itemView.findViewById (R.id.tvCompany);
        }
    }
}