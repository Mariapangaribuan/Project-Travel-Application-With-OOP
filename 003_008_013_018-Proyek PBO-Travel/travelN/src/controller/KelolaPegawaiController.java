package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.pegawai;
import app.Conecct;

public class KelolaPegawaiController implements Initializable {

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
    private TableColumn<pegawai, String> col_birth;

    @FXML
    private TableColumn<pegawai, Integer> col_id;

    @FXML
    private TableColumn<pegawai, String> col_nama;
    @FXML
    private DatePicker isi_birth;
    @FXML
    private TextField isi_id;

    @FXML
    private TextField isi_nama;

    @FXML
    private TableView<pegawai> table_pegawai;

    Connection con;
    java.sql.PreparedStatement pst;
    int myIndex;
    int id;

    ObservableList<pegawai> pegawais = FXCollections.observableArrayList();

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

    // Update pegawai
    @FXML
    public void Update(MouseEvent event) {
        myIndex = table_pegawai.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_pegawai.getItems().get(myIndex).getId(); // Get the ID of the selected pegawai

            String name = isi_nama.getText();
            LocalDate selectedDate = isi_birth.getValue();

            if (selectedDate != null) {
                String birth = selectedDate.toString();

                try {
                    pst = con.prepareStatement("update pegawai set nama = ?, birth = ? where id = ?");
                    pst.setString(1, name);
                    pst.setString(2, birth);
                    pst.setInt(3, id);

                    pst.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("pegawai Updated");
                    alert.setHeaderText("pegawai Updated");
                    alert.setContentText("Updated!");
                    alert.showAndWait();

                    // Update TableView
                    table();
                } catch (SQLException ex) {
                    System.err.println("Error" + ex);
                }
            } else {
                // Show a warning if no date is selected
                showAlert("Peringatan", "Pilih Tanggal",
                        "Silakan pilih tanggal untuk pegawai yang ingin diperbarui.");
            }
        } else {
            // Show a warning if no pegawai is selected
            showAlert("Peringatan", "Pilih pegawai", "Silakan pilih pegawai yang ingin diperbarui.");
        }
    }

    public void table() {
        isi_id.setDisable(false);
        clear_table(null);
        ObservableList<pegawai> pegawais = FXCollections.observableArrayList();

        try {
            pst = con
                    .prepareStatement("select id, nama, DATE_FORMAT(birth, '%Y-%m-%d') as formatted_date FROM pegawai");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                pegawai pegawai = new pegawai(rs.getInt("id"), rs.getString("nama"), rs.getString("formatted_date"));
                pegawais.add(pegawai);
            }
            table_pegawai.setItems(pegawais);
            col_id.setCellValueFactory(f -> f.getValue().idProperty().asObject());
            col_nama.setCellValueFactory(f -> f.getValue().namaProperty());
            col_birth.setCellValueFactory(f -> f.getValue().birthProperty());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        table_pegawai.setRowFactory(tv -> {
            TableRow<pegawai> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {

                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    isi_id.setDisable(true);

                    myIndex = table_pegawai.getSelectionModel().getSelectedIndex();
                    isi_id.setText(table_pegawai.getItems().get(myIndex).idProperty().getValue().toString());
                    isi_nama.setText(table_pegawai.getItems().get(myIndex).getnama());
                    isi_birth.setPromptText(table_pegawai.getItems().get(myIndex).getbirth());
                }
            });
            return myRow;
        });
    }

    @FXML
    public void clear_table(MouseEvent event) {
        isi_id.clear();
        isi_nama.setText("");
        isi_birth.setValue(null);
        isi_nama.requestFocus();
        isi_id.setDisable(false);
        isi_birth.setPromptText("yyyy-mm-dd");
    }

    @FXML
    public void Addpegawai(MouseEvent event) {
        String nama = isi_nama.getText();
        LocalDate date = isi_birth.getValue();

        try {
            if (nama.isEmpty() || date == null || isi_id.getText().isEmpty()) {
                showAlert("Success", "Isian Kosong", "Pastikan semua isian terisi.");
            } else {
                String formattedDate = date.toString();

                pst = con.prepareStatement("insert into pegawai(id,nama, birth) values (?, ?,?)");
                pst.setString(1, isi_id.getText());
                pst.setString(2, nama);
                pst.setString(3, formattedDate);

                pst.executeUpdate();

                showAlert("", "Penambahan Pegawai", "Pegawai has been successfully added!");

                table();

                isi_nama.clear();
                isi_birth.setValue(null);
                isi_nama.requestFocus();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Database Error", "Error adding pegawai: " + ex.getMessage());
        }
    }

   @FXML
public void Delete(MouseEvent event) {
    myIndex = table_pegawai.getSelectionModel().getSelectedIndex();
    if (myIndex != -1) {
        id = table_pegawai.getItems().get(myIndex).getId();

        // Menampilkan konfirmasi sebelum menghapus
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Anda yakin ingin menghapus pegawai?");
        confirmAlert.setContentText("Pilih OK untuk menghapus, Cancel untuk batal.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                pst = con.prepareStatement("delete from pegawai where id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setTitle("Pegawai Deleted");
                successAlert.setHeaderText("Pegawai Deleted");
                successAlert.setContentText("Deleted!");
                successAlert.showAndWait();

                table();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    } else {
        showAlert("Peringatan", "Pilih pegawai", "Silakan pilih pegawai yang ingin dihapus.");
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
