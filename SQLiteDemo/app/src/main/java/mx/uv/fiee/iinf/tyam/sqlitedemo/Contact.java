package mx.uv.fiee.iinf.tyam.sqlitedemo;

public class Contact {
    private int id;
    private String name;
    private String phone;

    public Contact () {}

    public Contact (int _id, String _name, String _phone) {
        id = _id;
        name = _name;
        phone = _phone;
    }

    public Contact (String _name, String _phone) {
        name = _name;
        phone = _phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
