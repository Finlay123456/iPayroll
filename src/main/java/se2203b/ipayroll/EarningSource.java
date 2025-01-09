package se2203b.ipayroll;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EarningSource {
    private final StringProperty code;
    private final StringProperty name; //Wages, Commissions, or Bonuses

    public EarningSource(){
        this.code = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
    }

    public void setCode(String code) {
        this.code.set(code);
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public String getCode() {
        return code.get();
    }
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty codeProperty() {
        return code;
    }

}
