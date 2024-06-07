package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class pegawai {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty birth = new SimpleStringProperty();

    public pegawai(int id, String nama, String birth) {
        setId(id);
        setnama(nama);
        setbirth(birth);
    }    


    public final StringProperty namaProperty() {
        return nama;
    }

    public final String getnama() {
        return namaProperty().get();
    }

    public final void setnama(String newnama) {
        namaProperty().set(newnama);
    }

    public final StringProperty birthProperty() {
        return birth;
    }

    public final String getbirth() {
        return birthProperty().get();
    }

    public final void setbirth(String birth) {
        birthProperty().set(birth);
    }

    public final int getId() {
        return id.get();
    }

    public final void setId(int Id) {
        id.set(Id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
}
 