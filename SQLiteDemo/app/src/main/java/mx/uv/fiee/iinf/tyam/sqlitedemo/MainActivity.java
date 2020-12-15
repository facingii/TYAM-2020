package mx.uv.fiee.iinf.tyam.sqlitedemo;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ContactsDatabase database;
    ArrayList<Contact> contacts;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        database = new ContactsDatabase (this);
        contacts = new ArrayList<> ();
        recyclerView = findViewById (R.id.contactList);
        recyclerView.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //inicializar
        if (database.getContactsCount ()  == 0) {
            database.addContact (new Contact ("Diana Montejo", "1234523"));
            database.addContact (new Contact ("Esther Narvaez", "5433214"));
            database.addContact (new Contact ("Carlos Cerón", "4565767"));
            database.addContact (new Contact ("Felipe Marín", "24256572"));
            database.addContact (new Contact ("Abigail Zamora", "34243654"));
            database.addContact (new Contact ("Manuel Cartas", "456456457"));
            database.addContact (new Contact ("David Velazquez", "456545657"));
        }
    }

    @Override
    protected void onResume () {
        super.onResume();

        contacts.clear ();
        contacts.addAll (database.getAllContacts ());

        recyclerView.setAdapter (new MyContactsAdapter (this, contacts));
    }

}

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
        myContactsVH.bind (contact.getName (), contact.getPhone ());
    }

    @Override
    public int getItemCount () {
        return contacts.size ();
    }
}

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