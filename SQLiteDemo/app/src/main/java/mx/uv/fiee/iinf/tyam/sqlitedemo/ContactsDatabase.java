package mx.uv.fiee.iinf.tyam.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Clase auxiliar para la conexión con Sqlite3 y el manejo de operaciones crud.
 * Para uso de datos básico Sqlite es suficiente pero en caso de grandes volúmenes de datos existen
 * mejores opciones, por ejemplo https://realm.io/
 *
 */
public class ContactsDatabase extends SQLiteOpenHelper {
    public static final String TAG = "CONTACTS_DBHELPER";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";

    //
    // CREATE TABLE contacts (id INTEGER PRIMARY KEY, name TEXT, phone TEXT)
    //
    private static String CREATE_CONTACS_TABLE =
                    "CREATE TABLE " + TABLE_CONTACTS +
                    " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_NAME + " TEXT, " +
                    KEY_PHONE + " TEXT" +
                    ")";


    private static String DROP_CONTACTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTACTS;

    public ContactsDatabase (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL (CREATE_CONTACS_TABLE);
        Log.d (TAG, "Creating Database...");
    }

    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL (DROP_CONTACTS_TABLE);
        onCreate (sqLiteDatabase);
        Log.d (TAG, "Upgrading database...");
    }


    // CRUD Operations

    public void addContact (Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase ();

        ContentValues values =  new ContentValues ();
        values.put (KEY_NAME, contact.getName ());
        values.put (KEY_PHONE, contact.getPhone ());

        db.insert (TABLE_CONTACTS, null, values);
        db.close ();
    }


    public Contact getContact (int id) {
        SQLiteDatabase db = this.getReadableDatabase ();
        Contact contact = new Contact ();

        String [] columns = {KEY_ID, KEY_NAME, KEY_PHONE};
        String selection = KEY_ID + "= ?";
        String [] selectionArgs = {String.valueOf (id)};

        Cursor cursor = db.query (TABLE_CONTACTS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst ();

            contact.setId (cursor.getInt (0));
            contact.setName (cursor.getString (1));
            contact.setPhone (cursor.getString (2));

            cursor.close ();
        }
        db.close ();

        return contact;
    }

    public List<Contact> getAllContacts () {
        List<Contact> contactList = new ArrayList<> ();
        String query = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery (query, null);

        if (cursor != null && cursor.moveToFirst ()) {
            do {
                Contact contact = new Contact ();
                contact.setId (cursor.getInt (0));
                contact.setName (cursor.getString (1));
                contact.setPhone (cursor.getString (2));

                contactList.add (contact);
            } while (cursor.moveToNext ());

            cursor.close ();
        }
        db.close ();

        return contactList;
    }

    public int getContactsCount () {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getReadableDatabase ();
        Cursor cursor = db.rawQuery (countQuery, null);
        int count = cursor.getCount ();

        cursor.close ();
        db.close ();

        return count;
    }

    public int updateContact (Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase ();

        ContentValues values = new ContentValues ();
        values.put (KEY_NAME, contact.getName ());
        values.put (KEY_PHONE, contact.getPhone ());

        String whereClause = KEY_ID + " = ?";
        String [] whereArgs = {String.valueOf (contact.getId ())};

        int result = db.update (TABLE_CONTACTS, values, whereClause, whereArgs);

        db.close ();
        return result;
    }

    public void deleteContact (Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase ();

        String whereClause = KEY_ID + " = ?";
        String [] whereArgs = {String.valueOf (contact.getId ())};
        db.delete (TABLE_CONTACTS, whereClause, whereArgs);
        db.close ();
    }

}
