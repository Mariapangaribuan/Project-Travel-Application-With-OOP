package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.kendaraan;
import app.Conecct;

public class KelolaKendaraanController implements Initializable {
    @FXML
    private Button back;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_tambah;

    @FXML
    private TableColumn<kendaraan, Integer> col_id;

    @FXML
    private TableColumn<kendaraan, String> col_jumlah;

    @FXML
    private TableColumn<kendaraan, String> col_nama;



    @FXML
    private TextField isi_jumlah;

    @FXML
    private TextField isi_nama;

    @FXML
    private TableView<kendaraan> table_kendaraan;

    Connection con;
    java.sql.PreparedStatement pst;
    int myIndex;
    int id;

    ObservableList<kendaraan> kendaraans = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = Conecct.connectDB();
        table();
    }

    @FXML
    private void closeCurrentStage(MouseEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void back(MouseEvent event) {
        try {
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getLogout(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Apakah Anda yakin ingin logout?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Update kendaraan
    @FXML
    public void Update(MouseEvent event) {
        myIndex = table_kendaraan.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_kendaraan.getItems().get(myIndex).getId(); // Get the ID of the selected kendaraan

            String name = isi_nama.getText();
            String jumlah = isi_jumlah.getText();

            try {
                pst = con.prepareStatement("update kendaraan set nama = ?, jumlah = ? where id = ?");
                pst.setString(1, name);
                pst.setString(2, jumlah);
                pst.setInt(3, id);

                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("kendaraan Updated");
                alert.setHeaderText("kendaraan Registration");
                alert.setContentText("Updated!");
                alert.showAndWait();

                // Update TableView
                table();
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            showAlert("Warning", "Pilih kendaraan", "Silakan pilih kendaraan yang ingin diubah.");
        }

    }

    public void table() {
        clear_table(null);
        ObservableList<kendaraan> kendaraans = FXCollections.observableArrayList();

        try {
            pst = con
                    .prepareStatement(
                            "select id, nama,jumlah FROM kendaraan");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                kendaraan kendaraan = new kendaraan(rs.getInt("id"), rs.getString("nama"), rs.getString("jumlah"));
                kendaraans.add(kendaraan);
            }
            table_kendaraan.setItems(kendaraans);
            col_nama.setCellValueFactory(f -> f.getValue().namaProperty());
            col_jumlah.setCellValueFactory(f -> f.getValue().jumlahProperty());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        table_kendaraan.setRowFactory(tv -> {
            TableRow<kendaraan> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {

                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {

                    myIndex = table_kendaraan.getSelectionModel().getSelectedIndex();
                    isi_nama.setText(table_kendaraan.getItems().get(myIndex).getnama());
                    isi_jumlah.setText(table_kendaraan.getItems().get(myIndex).getjumlah());
                }
            });
            return myRow;
        });
    }

    @FXML
    public void clear_table(MouseEvent event) {
        isi_nama.setText("");
        isi_jumlah.setText("");
        isi_nama.requestFocus();
    }

    @FXML
    public void Addkendaraan(MouseEvent event) {
        String nama = isi_nama.getText();
        String jumlah = isi_jumlah.getText();

        try {
            if (nama.isEmpty() || jumlah.isEmpty()) {
                showAlert("Warning", "Isian Kosong", "Pastikan semua isian terisi.");
            } else {
                pst = con.prepareStatement("insert into kendaraan(nama, jumlah) values (?, ?)");

                pst.setString(1, nama);
                pst.setString(2, jumlah);
                pst.executeUpdate();

                showAlert("successfully", "Penambahan kendaraan", "kendaraan has been successfully added!");

                table();

                isi_nama.clear();
                isi_nama.requestFocus();
                isi_jumlah.clear();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Database Error", "Error adding kendaraan: " + ex.getMessage());
        }
    }
@FXML
public void Delete(MouseEvent event) {
    myIndex = table_kendaraan.getSelectionModel().getSelectedIndex();
    if (myIndex != -1) {
        id = table_kendaraan.getItems().get(myIndex).getId();

        // Menampilkan konfirmasi sebelum menghapus
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Anda yakin ingin menghapus kendaraan?");
        confirmAlert.setContentText("Pilih OK untuk menghapus, Cancel untuk batal.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                pst = con.prepareStatement("delete from kendaraan where id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("kendaraan Deleted");
                successAlert.setHeaderText("kendaraan Deleted");
                successAlert.setContentText("Deleted!");
                successAlert.showAndWait();

                table();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    } else {
        showAlert("Peringatan", "Pilih kendaraan", "Silakan pilih kendaraan yang ingin dihapus.");
    }
}


    private void showAlert(String nama, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(nama);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
