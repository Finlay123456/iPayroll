package se2203b.ipayroll;

import javafx.beans.property.*;

import java.util.Objects;

public class IPayrollUser {
    private StringProperty id;
    private StringProperty fullName;
    private ObjectProperty<UserAccount> userAccount
            = new SimpleObjectProperty(new UserAccount());

    //set and get methods
    // id property
    public void setID(String id) {
        this.id = new SimpleStringProperty(id);
    }
    public StringProperty idProperty() {
        return this.id;
    }
    public String getID() {
        return this.id.get();
    }

    // name property
    public void setFullName(String name) {
        this.fullName = new SimpleStringProperty(name);
    }
    public StringProperty fullNameProperty() {
        return this.fullName;
    }
    public String getFullName() {
        return this.fullName.get();
    }

    // userAccount Property
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount.set(userAccount);
    }
    public ObjectProperty<UserAccount> userAccountProperty() {
        return this.userAccount;
    }
    public UserAccount getUserAccount() {
        return this.userAccount.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPayrollUser that = (IPayrollUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
