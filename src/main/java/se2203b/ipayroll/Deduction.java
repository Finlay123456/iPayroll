package se2203b.ipayroll;

import java.sql.Date;
import javafx.beans.property.*;

public class Deduction {
    private final StringProperty deductionID;
    private final DoubleProperty amount;
    private final DoubleProperty percentOfEarnings;
    private final ObjectProperty<Date> startDate;
    private final ObjectProperty<Date> stopDate;
    private final DoubleProperty upperLimit;
    private final ObjectProperty<DeductionSource> deductionSource;
    private final ObjectProperty<Employee> employee;

    public Deduction(){
        this.employee = new SimpleObjectProperty<>();
        this.deductionID = new SimpleStringProperty();
        this.amount = new SimpleDoubleProperty();
        this.percentOfEarnings = new SimpleDoubleProperty();
        this.startDate = new SimpleObjectProperty<>();
        this.stopDate = new SimpleObjectProperty<>();
        this.upperLimit = new SimpleDoubleProperty();
        this.deductionSource = new SimpleObjectProperty<>();
    }

    // Get methods
    public String getDeductionID() { return deductionID.get(); }
    public double getAmount() { return amount.get(); }
    public double getPercentOfEarnings() { return percentOfEarnings.get(); }
    public Date getStartDate() { return startDate.get(); }
    public Date getStopDate() { return stopDate.get(); }
    public double getUpperLimit() { return upperLimit.get(); }
    public DeductionSource getDeductionSource() { return deductionSource.get(); }
    public Employee getEmployee() {
        return employee.get();
    }

    // Set methods
    public void setDeductionID(String id) { deductionID.set(id); }
    public void setAmount(double amt) { amount.set(amt); }
    public void setPercentOfEarnings(double percent) { percentOfEarnings.set(percent); }
    public void setStartDate(Date date) { startDate.set(date); }
    public void setStopDate(Date date) { stopDate.set(date); }
    public void setUpperLimit(double limit) { upperLimit.set(limit); }
    public void setDeductionSource(DeductionSource source) { deductionSource.set(source); }
    public void setEmployee(Employee employee){
        this.employee.set(employee);
    }

    // Property methods
    public StringProperty deductionIDProperty() { return deductionID; }
    public DoubleProperty amountProperty() { return amount; }
    public DoubleProperty percentOfEarningsProperty() { return percentOfEarnings; }
    public ObjectProperty<Date> startDateProperty() { return startDate; }
    public ObjectProperty<Date> stopDateProperty() { return stopDate; }
    public DoubleProperty upperLimitProperty() { return upperLimit; }
    public ObjectProperty<DeductionSource> deductionSourceProperty() { return deductionSource; }
    public ObjectProperty<Employee> employeeProperty(){
        return employee;
    }
}