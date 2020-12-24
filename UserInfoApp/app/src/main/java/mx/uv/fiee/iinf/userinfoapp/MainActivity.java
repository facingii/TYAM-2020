package mx.uv.fiee.iinf.userinfoapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Locale;
import java.util.Vector;

public class MainActivity extends Activity {

    RecyclerView recyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    UsersAdapter adapter;
    Vector<User> vector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        Toolbar toolbar = findViewById (R.id.toolbar);
        toolbar.setTitle ("Usuarios");
        setActionBar (toolbar);

        vector = new Vector<> ();
        adapter = new UsersAdapter (vector);

        recyclerView = findViewById (R.id.rvUsers);
        recyclerView.addItemDecoration (new DividerItemDecoration (this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator (new DefaultItemAnimator ());
        recyclerView.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter (adapter);

        auth = FirebaseAuth.getInstance ();
        database = FirebaseDatabase.getInstance ();
    }

    @Override
    protected void onResume() {
        super.onResume ();

        login ();
        getUsers ();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.main, menu);
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId () == R.id.mnuNewUser) {
            Intent intent = new Intent (getBaseContext (), FormActivity.class);
            startActivity (intent);
        }

        return super.onOptionsItemSelected (item);
    }

    private void login () {
        auth.signInAnonymously ()
                .addOnSuccessListener(authResult -> Log.i ("TYAM", authResult.toString ()))
                .addOnFailureListener(e -> Log.e ("TYAM", e.getMessage ()));
    }

    private void getUsers () {
        Snackbar snackbar = Snackbar.make (recyclerView, "Obteniendo información...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup layer = (ViewGroup) snackbar.getView ().findViewById (com.google.android.material.R.id.snackbar_text).getParent ();
        ProgressBar bar = new ProgressBar (getBaseContext ());
        layer.addView (bar);
        snackbar.show ();

        DatabaseReference reference = database.getReference ("Usuarios");
        //Vector<User> users = new Vector<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren ()) {
                    User u = snap.getValue (User.class);
                    vector.add (u);
                }

                //recyclerView.setAdapter (new UsersAdapter (users));
                adapter.notifyDataSetChanged ();
                snackbar.dismiss ();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                Log.e ("TYAM", error.getDetails ());
                snackbar.dismiss ();
            }
        });
    }
}

class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersVH> {
    private final Vector<User> users;

    public UsersAdapter (Vector<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.list_item, parent, false);
        return new UsersVH (view);
    }

    @Override
    public void onBindViewHolder (@NonNull UsersAdapter.UsersVH holder, int position) {
        User u = users.get (position);

        holder.tvNameListItem.setText (String.format (Locale.getDefault (), "%s %s", u.nombre, u.apellidos));
        holder.tvAgeListItem.setText (String.valueOf (u.edad));
        holder.tvTelephoneListItem.setText (u.telefono);
        holder.tvAdressListItem.setText (u.direccion);

        holder.setPicture (Uri.parse (u.foto));
    }

    @Override
    public int getItemCount () {
        return users.size ();
    }


    class UsersVH extends RecyclerView.ViewHolder {
        private final ImageView ivProfilePicListItem;
        public TextView tvNameListItem, tvAgeListItem, tvTelephoneListItem, tvAdressListItem;

        public UsersVH (@NonNull View itemView) {
            super (itemView);

            ivProfilePicListItem = itemView.findViewById (R.id.ivProfilePicListItem);
            tvNameListItem = itemView.findViewById (R.id.tvNameListItem);
            tvAgeListItem = itemView.findViewById (R.id.tvAgeListItem);
            tvTelephoneListItem = itemView.findViewById (R.id.tvTelephoneListItem);
            tvAdressListItem = itemView.findViewById (R.id.tvAddressListItem);
        }

        public void setPicture (Uri url) {
            Picasso.get()
                    .load (url)
                    .into (ivProfilePicListItem);
        }
    }
}