package se2203b.ipayroll;

import javafx.beans.property.*;

public class DeductionSource {
    private final StringProperty code;
    private final StringProperty name; //Income Taxes, Insurance, Pension, Federal Taxes, or Provincial Taxes

    public DeductionSource(){
        code = new SimpleStringProperty();
        name = new SimpleStringProperty();
    }

    // Get methods
    public String getCode() { return code.get(); }
    public String getName() { return name.get(); }

    // Set methods
    public void setCode(String c) { code.set(c); }
    public void setName(String n) { name.set(n); }

    // Property methods
    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
}
