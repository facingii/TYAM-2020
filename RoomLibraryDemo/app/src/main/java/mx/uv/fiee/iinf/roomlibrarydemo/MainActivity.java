package mx.uv.fiee.iinf.roomlibrarydemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Contact> contacts;
    AppDatabase database;
    RecyclerView contactList;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        contactList = findViewById (R.id.contactList);
        contactList.setLayoutManager (new LinearLayoutManager (this, RecyclerView.VERTICAL, false));

        database = Room.databaseBuilder (this, AppDatabase.class, "myContacts").allowMainThreadQueries().build ();

        contacts = (ArrayList<Contact>) database.contactDao().getAll ();
        if (contacts.size () == 0) {
            database.contactDao().insertAll (
                    new Contact ("Diana Montejo", "1234523"),
                    new Contact ("Esther Narvaez", "5433214"),
                    new Contact ("Carlos Cerón", "4565767"),
                    new Contact ("Felipe Marín", "24256572"),
                    new Contact ("Abigail Zamora", "34243654"),
                    new Contact ("Manuel Cartas", "456456457"));
        }
    }

    @Override
    protected void onResume () {
        super.onResume();

        contacts.clear ();
        contacts.addAll (database.contactDao().getAll ());
        contactList.setAdapter (new MyContactsAdapter (this, contacts));
    }
}

/**
 * Adaptador con información de los contactos
 */
class MyContactsAdapter extends RecyclerView.Adapter<MyContactsVH> {
    private Context context;
    private ArrayList<Contact> contacts;

    MyContactsAdapter (Context context, ArrayList<Contact> contacts) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyContactsVH onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from (context);
        View view = inflater.inflate (android.R.layout.simple_list_item_2, viewGroup, false);

        return new MyContactsVH (view);
    }

    @Override
    public void onBindViewHolder (@NonNull MyContactsVH myContactsVH, int i) {
        Contact contact = contacts.get (i);
        myContactsVH.bind (contact.fullName, contact.phoneNumber);
    }

    @Override
    public int getItemCount () {
        return contacts.size ();
    }
}

/**
 * ViewHolder, reutiliza las vistas para optimizar el rendimiento
 */
class MyContactsVH extends RecyclerView.ViewHolder {
    private TextView text1;
    private TextView text2;

    MyContactsVH (@NonNull View itemView) {
        super (itemView);
        text1 = itemView.findViewById (android.R.id.text1);
        text2 = itemView.findViewById (android.R.id.text2);
    }

    void bind (String t1, String t2) {
        text1.setText (t1);
        text2.setText (t2);
    }
}