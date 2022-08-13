package controllers;

import Dao.DBConnect;
import application.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.UserModel;
import models.BankModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminController {

	// Declare DB objects
	Statement stmt = null;
	Connection conn = null;
	
	@FXML
	private TextField txtUserName;
	
	@FXML
	private TextField txtBankName;
	
	@FXML
	private ComboBox<String> cmbRole;

	@FXML
	private TextField txtAddress;

	@FXML
	private TableView<BankModel> bankTable;

	@FXML
	private TableView<UserModel> userTable;

	@FXML
	private TableColumn<UserModel,String> colUserId;

	@FXML
	private TableColumn<UserModel,String> colUserName;

	@FXML
	private TableColumn<UserModel,String> colUserRole;

	@FXML
	private TableColumn<BankModel,String> colBankId;

	@FXML
	private TableColumn<BankModel,String> colBankName;

	@FXML
	private TableColumn<BankModel,String> colBankAddress;

	@FXML
	private Label lblBankError;

	@FXML
	private Label lblUserError;

	@FXML
	private Label lbUserId;

	@FXML
	private Label lbBankId;

	public AdminController() throws SQLException {
		DBConnect dbConnect = new DBConnect();
		conn = dbConnect.getConnection();
		stmt = conn.createStatement();
	}

	@FXML
	void initialize() throws SQLException {
		colUserId.setCellValueFactory(new PropertyValueFactory("id"));
		colUserName.setCellValueFactory(new PropertyValueFactory("uname"));
		colUserRole.setCellValueFactory(new PropertyValueFactory("admin"));
		colBankId.setCellValueFactory(new PropertyValueFactory("bankId"));
		colBankName.setCellValueFactory(new PropertyValueFactory("bankName"));
		colBankAddress.setCellValueFactory(new PropertyValueFactory("bankAddress"));

		refreshUserData();
		refreshBankData();
		cmbRole.getItems().addAll("Admin", "User");
	}


	public void addUser(ActionEvent event) {
		System.out.println("Inserting records into the table...");
		String username = txtUserName.getText();
		if(null == username || username.isEmpty()){
			lblUserError.setText("The User Name is Empty!");
			return;
		}
		String role = cmbRole.getValue();
		if(null == role || role.isEmpty()){
			lblUserError.setText("The role is Empty!");
			return;
		} else {
			if(role.equals("Admin")){
				role = "1";
			} else {
				role = "0";
			}
		}
		String password = Utils.encrypt2MD5("123456");
		try{
			int userId = getMaxUserId() + 1;
			String insertSQL = "insert into jpapa_users(id,uname,passwd,admin) values ('" + userId + "','"+ username + "','" + password + "','" +
					role + "')";
			stmt.executeUpdate(insertSQL);
			System.out.println("User Record created");

			String insertAccountSQL = "insert into jpapa_accounts(type,balance,userid,amount) values ('" + "Create" + "','" + "0.00" + "','"
					+ userId + "','" + "0.00" + "')";
			stmt.executeUpdate(insertAccountSQL);
			System.out.println("Account Record created");

			refreshUserData();


		} catch (SQLException e){
			System.out.println("User creation failed");
			e.printStackTrace();
		}



	}

	public void updateUser(ActionEvent event) {
		System.out.println("Updating records into the table...");
		String username = txtUserName.getText();
		if(null == username || username.isEmpty()){
			lblUserError.setText("The User Name is Empty!");
			return;
		}
		String role = cmbRole.getValue();
		if(null == role || role.isEmpty()){
			lblUserError.setText("The role is Empty!");
			return;
		}
		String accountId = lbUserId.getText();
		if(null == accountId || accountId.isEmpty()){
			lblUserError.setText("Please select one!");
			return;
		}
		try{
			String updateSQL = "update jpapa_users set uname = '" + username + "', admin = '" + role + "' where id = '" + accountId + "'";
			stmt.executeUpdate(updateSQL);
			refreshUserData();
			System.out.println("User Record created");
		} catch (SQLException e){
			System.out.println("User creation failed");
			e.printStackTrace();
		}
	}

	public void deleteUser(ActionEvent event) {
		String accountId = lbUserId.getText();
		if(null == accountId || accountId.isEmpty()){
			lblUserError.setText("Please select one!");
			return;
		}
		try{
			String updateSQL = "delete from jpapa_users where id = '" + accountId + "'";
			stmt.executeUpdate(updateSQL);
			System.out.println("User Record deleted");
			txtUserName.setText("");
			cmbRole.setValue("");
			refreshUserData();
		} catch (SQLException e){
			System.out.println("User Record delete failed");
			e.printStackTrace();
		}
	}

	public void cancelUser(ActionEvent event) {
		txtUserName.setText("");
		cmbRole.setValue("");
	}

	public void addBank(ActionEvent event) {
		System.out.println("Inserting records into the table...");
		String bankName = txtBankName.getText();
		String address = txtAddress.getText();
		if(null == bankName || bankName.isEmpty()){
			lblBankError.setText("The bank name is Empty!");
			return;
		}
		if(null == address || address.isEmpty()){
			lblBankError.setText("The address is Empty!");
			return;
		}

		try{
			String insertSQL = "insert into jpapa_bank(name,address) values ('" + bankName + "','" + address + "')";
			stmt.executeUpdate(insertSQL);
			System.out.println("Bank Record created");
			refreshBankData();
		} catch (SQLException e){
			System.out.println("Bank Record creation failed");
			e.printStackTrace();
		}

	}

	public void updateBank(ActionEvent event) {
		System.out.println("Updating records into the table...");
		String bankName = txtBankName.getText();
		String address = txtAddress.getText();
		if(null == bankName || bankName.isEmpty()){
			lblBankError.setText("The bank name is Empty!");
			return;
		}
		if(null == address || address.isEmpty()){
			lblBankError.setText("The address is Empty!");
			return;
		}

		String bankId = lbBankId.getText();
		if(null == bankId || bankId.isEmpty()){
			lblBankError.setText("Please select one!");
			return;
		}
		try{
			String updateSQL = "update jpapa_bank set name = '" + bankName + "',address = '" + address + "' where id = '" + bankId + "'";
			stmt.executeUpdate(updateSQL);
			System.out.println("Bank Record updated");
			refreshBankData();
		} catch (SQLException e){
			System.out.println("Bank Record update failed");
			e.printStackTrace();
		}
	}

	public void deleteBank(ActionEvent event) {
		String bankId = lbBankId.getText();
		if(null == bankId || bankId.isEmpty()){
			lblBankError.setText("Please select one!");
			return;
		}
		try{
			String updateSQL = "delete from jpapa_bank where id = '" + bankId + "'";
			stmt.executeUpdate(updateSQL);
			System.out.println("Bank Record deleted");
			txtBankName.setText("");
			txtAddress.setText("");
			refreshBankData();
		} catch (SQLException e){
			System.out.println("Bank Record delete failed");
			e.printStackTrace();
		}
	}

	public void cancelBank(ActionEvent event) {
		txtBankName.setText("");
		txtAddress.setText("");
	}

	public void selectUser(MouseEvent mouseEvent) {
		UserModel account = userTable.getSelectionModel().getSelectedItem();
		txtUserName.setText(account.getUname());
		if(account.getAdmin().equals("Admin")){
			cmbRole.setValue("Admin");
		} else {
			cmbRole.setValue("User");
		}
		lbUserId.setText(account.getId());
	}

	public void selectBank(MouseEvent mouseEvent) {
		BankModel bank = bankTable.getSelectionModel().getSelectedItem();
		txtBankName.setText(bank.getBankName());
		txtAddress.setText(bank.getBankAddress());
		lbBankId.setText(String.valueOf(bank.getBankId()));
	}

	private void refreshUserData() throws SQLException {
		userTable.getItems().clear();
		String accountQuerySQL = "SELECT id,uname,admin FROM jpapa_users";
		ObservableList<UserModel> accountList = FXCollections.observableArrayList();
		ResultSet accountRs = stmt.executeQuery(accountQuerySQL);
		while (accountRs.next()) {
			UserModel account = new UserModel();
			String id = accountRs.getString("id");
			String userName = accountRs.getString("uname");
			String admin = accountRs.getString("admin");
			if(admin.equals("1")){
				account.setAdmin("Admin");
			} else {
				account.setAdmin("User");
			}
			account.setId(id);
			account.setUname(userName);
			accountList.add(account);
		}
		userTable.setItems(accountList);
	}

	private void refreshBankData() throws SQLException {
		bankTable.getItems().clear();
		ObservableList<BankModel> bankList = FXCollections.observableArrayList();
		String bankQuerySQL = "SELECT id,name,address FROM jpapa_bank";
		ResultSet bankRs = stmt.executeQuery(bankQuerySQL);
		while (bankRs.next()) {
			int id = bankRs.getInt("id");
			String name = bankRs.getString("name");
			String address = bankRs.getString("address");
			BankModel bank = new BankModel(id,name,address);
			bankList.add(bank);
		}
		bankTable.setItems(bankList);
	}

	private int getMaxUserId() throws SQLException {
		String querySQL = "SELECT MAX(id) maxId FROM jpapa_users";
		ResultSet rs = stmt.executeQuery(querySQL);
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("maxId");
		}
		return id;
	}
}
