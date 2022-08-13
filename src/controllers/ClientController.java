package controllers;

import Dao.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.AccountModel;
import models.BankModel;
import models.UserModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

	private Statement stmt = null;
	private Connection conn = null;

	private static int userId;

	@FXML
	public TableColumn<AccountModel,String> colId;
	@FXML
	public TableColumn<AccountModel,String> colAmount;
	@FXML
	public TableColumn<AccountModel,String> colType;
	@FXML
	public TableView<AccountModel> accountTableView;
	@FXML
	public TextField txtAmount;
	@FXML
	public ComboBox<String> cmbType;
	@FXML
	public TextField txtBalance;
	@FXML
	public Button btnCancel;
	@FXML
	public Label accountId;
	@FXML
	public Button btnConfirm;
	@FXML
	public Label lblError;

	public ClientController() throws SQLException {
		DBConnect dbConnect = new DBConnect();
		conn = dbConnect.getConnection();
		stmt = conn.createStatement();
	}

	public void initialize(URL location, ResourceBundle resources) {
		colId.setCellValueFactory(new PropertyValueFactory("id"));
		colAmount.setCellValueFactory(new PropertyValueFactory("type"));
		colType.setCellValueFactory(new PropertyValueFactory("amount"));

		refreshAccData();
		cmbType.getItems().addAll("Withdraw", "Deposit");
	}


	public void confirm(ActionEvent event) {
		System.out.println("Updating records into the table...");
		String amount = txtAmount.getText();
		String type = cmbType.getValue();
		if(null == amount || amount.isEmpty()){
			lblError.setText("The amount is Empty!");
			return;
		}
		if(null == type || type.isEmpty()){
			lblError.setText("The type is Empty!");
			return;
		}

		String id = accountId.getText();
		if(null == id || id.isEmpty()){
			lblError.setText("Please select one!");
			return;
		}
		String balanceTxt = txtBalance.getText();
		if(null == balanceTxt || balanceTxt.isEmpty()){
			balanceTxt = "0";
		}
		Double balanceD = Double.valueOf(balanceTxt);
		if (type.equals("Withdraw")){
			balanceD = balanceD - Double.valueOf(amount);
		} else {
			balanceD = balanceD + Double.valueOf(amount);
		}
		try{
			String insertSQL = "insert into jpapa_accounts(type,balance,userid,amount) values ('" + type + "','" + balanceD + "','"
					+ userId + "','" + amount + "')";
			stmt.executeUpdate(insertSQL);
			System.out.println("Account Record updated");
			refreshAccData();
		} catch (SQLException e){
			System.out.println("Account Record update failed");
			e.printStackTrace();
		}
		txtBalance.setText(String.valueOf(balanceD));

	}

	public void cancel(ActionEvent event) {
		txtAmount.setText("");
		cmbType.setValue("");
	}

	public void getAccountItem(MouseEvent mouseEvent) {
		AccountModel accountModel = accountTableView.getSelectionModel().getSelectedItem();
		accountId.setText(String.valueOf(accountModel.getId()));
	}

	private void refreshAccData(){
		accountTableView.getItems().clear();
		ObservableList<AccountModel> accountModels = FXCollections.observableArrayList();
		String accQuerySQL = "SELECT id,type,balance,userid,amount FROM jpapa_accounts WHERE userid = " + "'" +userId + "'" ;
		ResultSet accRs = null;
		try {
			accRs = stmt.executeQuery(accQuerySQL);
			while (accRs.next()) {
				int id = accRs.getInt("id");
				String type = accRs.getString("type");
				String amount = accRs.getString("amount");
				String balance = accRs.getString("balance");
				AccountModel accountModel = new AccountModel(id,  type, amount, balance);
				accountModels.add(accountModel);
				txtBalance.setText(String.valueOf(balance));
			}
			accountTableView.setItems(accountModels);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setUserId(int user_id) {
		userId = user_id;
		System.out.println("Welcome id " + userId);
	}
}
