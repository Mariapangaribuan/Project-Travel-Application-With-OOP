package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class kendaraan {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty jumlah = new SimpleStringProperty();

    public kendaraan(int id, String nama, String jumlah) {
        setId(id);
        setnama(nama);
        setjumlah(jumlah);
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

    public final StringProperty jumlahProperty() {
        return jumlah;
    }

    public final String getjumlah() {
        return jumlahProperty().get();
    }

    public final void setjumlah(String jumlah) {
        jumlahProperty().set(jumlah);
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
