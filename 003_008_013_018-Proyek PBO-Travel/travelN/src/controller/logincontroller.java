
package controller;

import java.net.URL;
import java.sql.ResultSet;

import java.util.ResourceBundle;
import javafx.stage.Stage;
import app.Conecct;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author T0MM11Y
 */
public class logincontroller implements Initializable {

    @FXML
    private Button btn_login;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    private Connection connect;
    private java.sql.PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;

    private void emptyFieldErrorMessage(String message) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("upps Error");
        alert.setHeaderText("Ada field kosong");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void successMessage(String message) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    public void login() {
        connect = Conecct.connectDB();

        try {
            if (email.getText().isEmpty() || password.getText().isEmpty()) {
                emptyFieldErrorMessage("Harap isi semua field");
            } else {
                String selectData = "SELECT * FROM user WHERE email=? AND password=?";

                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, email.getText());
                prepare.setString(2, password.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    successMessage("Login Berhasil");
                    Parent root = FXMLLoader.load(getClass().getResource("/view/DashboardView.fxml"));

                    Stage stage = new Stage();
                    stage.setTitle("Dashboard");
                    stage.setScene(new Scene(root));

                    stage.show();
                    btn_login.getScene().getWindow().hide();

                } else {
                    emptyFieldErrorMessage("email atau Password Salah");
                }

            }
        } catch (Exception e) {

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}