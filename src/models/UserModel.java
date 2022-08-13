package models;

public class UserModel {

	private String id;
	private String uname;
	private String password;
	private String admin;
	private String balance;

	public UserModel(){

	}

	public UserModel(String id, String uname, String password, String admin, String balance) {
		this.id = id;
		this.uname = uname;
		this.password = password;
		this.admin = admin;
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account{" +
				"id='" + id + '\'' +
				", uname='" + uname + '\'' +
				", password='" + password + '\'' +
				", admin='" + admin + '\'' +
				", balance='" + balance + '\'' +
				'}';
	}
}