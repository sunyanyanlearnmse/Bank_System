package models;

public class BankModel {

	int bankId;
	String bankName;
	String bankAddress;

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public BankModel(Integer id, String name, String address) {
		this.bankId = id;
		this.bankName = name;
		this.bankAddress = address;
	}

	public BankModel() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {

		String bankData = "Bank Account Affiliation:\nID- " + bankId;
		bankData += "\nName- " + bankName;
		bankData += "\nAddress-" + bankAddress + "\n";

		return bankData;
	}

}
