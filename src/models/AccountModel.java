package models;

public class AccountModel {

    private int id;

    private int userId;

    private String type;

    private String amount;

    private String balance;

    public AccountModel(int id, String type, String amount, String balance) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }

    public AccountModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
