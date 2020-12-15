package mx.uv.fiee.iinf.roomlibrarydemo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo (name = "full_name")
    public String fullName;

    @ColumnInfo (name = "phone_number")
    public String phoneNumber;

    public Contact () {}

    public Contact (String n, String p) {
        this.fullName = n;
        this.phoneNumber = p;
    }
}
