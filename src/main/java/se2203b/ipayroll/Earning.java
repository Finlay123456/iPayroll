package se2203b.ipayroll;

import javafx.beans.property.*;

import java.sql.Date;

public class Earning {

    private final StringProperty earningID;
    private final DoubleProperty amount;
    private final DoubleProperty ratePerHour;
    private final ObjectProperty<Date> startDate;
    private final ObjectProperty<Date> endDate;
    private final ObjectProperty<EarningSource> earningSource;
    private final ObjectProperty<Employee> employee;

    public Earning(){
        this.employee = new SimpleObjectProperty<>();
        this.earningID = new SimpleStringProperty();
        this.amount = new SimpleDoubleProperty();
        this.ratePerHour = new SimpleDoubleProperty();
        this.startDate = new SimpleObjectProperty<>();
        this.endDate = new SimpleObjectProperty<>();
        this.earningSource = new SimpleObjectProperty<>();
    }

    // Set methods
    public void setEarningID(String _code){
        earningID.set(_code);
    }
    public void setAmount(double amt){
        amount.set(amt);
    }
    public void setRatePerHour(double amt){
        ratePerHour.set(amt);
    }
    public void setStartDate(Date _date){
        startDate.set(_date);
    }
    public void setEndDate(Date _date){
        endDate.set(_date);
    }
    public void setEarningSource(EarningSource earningSource) {
        this.earningSource.set(earningSource);
    }
    public void setEmployee(Employee employee){
        this.employee.set(employee);
    }

    // Get methods
    public String getEarningID() {
        return earningID.get();
    }
    public double getAmount() {
        return amount.get();
    }
    public double getRatePerHour() {
        return ratePerHour.get();
    }
    public Date getStartDate() {
        return startDate.get();
    }
    public Date getEndDate() {
        return endDate.get();
    }
    public EarningSource getEarningSource() {
        return earningSource.get();
    }
    public Employee getEmployee() {
        return employee.get();
    }

    // Property methods
    public StringProperty earningIDProperty() {
        return earningID;
    }
    public DoubleProperty amountProperty() {
        return amount;
    }
    public DoubleProperty ratePerHourProperty() {
        return ratePerHour;
    }
    public ObjectProperty<Date> startDateProperty() {
        return startDate;
    }
    public ObjectProperty<Date> endDateProperty() {
        return endDate;
    }
    public ObjectProperty<EarningSource> earningSourceProperty() {
        return earningSource;
    }
    public ObjectProperty<Employee> employeeProperty(){
        return employee;
    }
}
