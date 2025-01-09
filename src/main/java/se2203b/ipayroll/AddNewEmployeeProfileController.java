/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.ipayroll;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;

/**
 * FXML Controller class
 *
 * @author Abdelkader
 */
public class AddNewEmployeeProfileController implements Initializable {

    @FXML
    private TabPane tb;
    @FXML
    private TextField id;
    @FXML
    private TextField fullName;
    @FXML
    private TextField city;
    @FXML
    private TextField province;
    @FXML
    private TextField phone;
    @FXML
    private TextField SIN;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField martialStatus;
    @FXML
    private TextField jobName;
    @FXML
    private TextField skillCode;
    @FXML
    private DatePicker DOB;
    @FXML
    private DatePicker DOH;
    @FXML
    private DatePicker DOLP;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button addDeductionBtn;
    @FXML
    private ComboBox<String> payTypeBox;
    @FXML
    private TextField workingText;
    @FXML
    private ComboBox<String> statementTypeBox;
    @FXML
    private CheckBox exemptCheckBox;
    @FXML
    private TableView<Earning> earningsTableView;
    @FXML
    private ComboBox<String> earningBox;
    @FXML
    private TableColumn<Earning, String> earningColumn;
    @FXML
    private TextField valueTextField;
    @FXML
    private TableColumn<Earning, Double> valueColumn;
    @FXML
    private TextField rphTextField;
    @FXML
    private TableColumn<Earning, Double> rphColumn;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TableColumn<Earning, Date> startColumn;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TableColumn<Earning, Date> endColumn;
    @FXML
    private TableView<Deduction> deductionsTableView;
    @FXML
    private ComboBox<String> deductionBox;
    @FXML
    private TableColumn<Deduction, String> deductionColumn;
    @FXML
    private TextField valueTextField2;
    @FXML
    private TableColumn<Deduction, Double> valueColumn2;
    @FXML
    private TextField poeTextField;
    @FXML
    private TableColumn<Deduction, Double> poeColumn;
    @FXML
    private TextField upperTextField;
    @FXML
    private TableColumn<Deduction, Double> upperColumn;
    @FXML
    private DatePicker startDatePicker2;
    @FXML
    private TableColumn<Deduction, Date> startColumn2;
    @FXML
    private DatePicker stopDatePicker;
    @FXML
    private TableColumn<Deduction, Date> stopColumn;


    private DataStore userAccountAdapter;
    private DataStore employeeTableAdapter;
    private DataStore earningTableAdapter;
    private DataStore earningSourceTableAdapter;
    private DataStore deductionTableAdapter;
    private DataStore deductionSourceTableAdapter;

    private IPayrollController iPAYROLLController;

    private final ObservableList<Earning> tempEarnings = FXCollections.observableArrayList();
    private final ObservableList<Employee> tempEmployees = FXCollections.observableArrayList();
    private final ObservableList<Deduction> tempDeductions = FXCollections.observableArrayList();

    public void setDataStore(DataStore account, DataStore profile, DataStore earnTable, DataStore eSourceTable, DataStore deductTable, DataStore dSourceTable) {
        userAccountAdapter = account;
        employeeTableAdapter = profile;
        earningTableAdapter = earnTable;
        earningSourceTableAdapter = eSourceTable;
        deductionTableAdapter = deductTable;
        deductionSourceTableAdapter = dSourceTable;
    }

    public void setIPayrollController(IPayrollController controller) {
        iPAYROLLController = controller;
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void save() {
        try {
            for(Employee emp : tempEmployees) {
                if(!((EmployeeTableAdapter)employeeTableAdapter).exists(emp.getID())){
                    employeeTableAdapter.addNewRecord(emp);
                }
                for(Earning earning : tempEarnings){
                    if(earning.getEmployee().equals(emp)) {
                        earningTableAdapter.addNewRecord(earning);
                    }
                }
            }
            tempEmployees.clear();
            tempEarnings.clear();

        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void add() throws SQLException {
        Employee tempEmployee = new Employee();
        UserAccount account = new UserAccount();
        tempEmployee.setID(this.id.getText());
        tempEmployee.setFullName(this.fullName.getText());
        tempEmployee.setCity(this.city.getText());
        tempEmployee.setProvince(this.province.getText());
        tempEmployee.setPostalCode(this.postalCode.getText());
        tempEmployee.setPhone(this.phone.getText());
        tempEmployee.setSIN(this.SIN.getText());
        tempEmployee.setMartialStatus(this.martialStatus.getText());
        tempEmployee.setJobName(this.jobName.getText());
        tempEmployee.setSkillCode(this.skillCode.getText());
        tempEmployee.setDOB(Date.valueOf(this.DOB.getValue()));
        tempEmployee.setDOH(Date.valueOf(this.DOH.getValue()));
        tempEmployee.setDOLP(Date.valueOf(this.DOLP.getValue()));
        tempEmployee.setPayType(this.payTypeBox.getValue());
        tempEmployee.setWorkHours(Double.parseDouble(this.workingText.getText()));
        tempEmployee.setEarningStatementType(statementTypeBox.getValue());
        tempEmployee.setExempt(exemptCheckBox.isSelected());
        tempEmployee.setUserAccount(account);

        if (!tempEmployees.contains(tempEmployee)) {
            tempEmployees.add(tempEmployee);
        }

        EarningSource tempEarningSource = new EarningSource();

        Earning tempEarning = new Earning();

        //getting the amount of earningSources and earnings in the db to create appropriate code/earningID for next source
        List<Object> sourceList, earnList;
        try {
            sourceList = earningSourceTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tempEarningSource.setCode(String.valueOf(sourceList.size()+1));

        String sourceName = this.earningBox.getValue(); // Get the value from the ComboBox
        if (sourceName != null && !sourceName.isEmpty()) {
            // Capitalize the first letter and make the rest lowercase
            sourceName = sourceName.substring(0, 1).toUpperCase() + sourceName.substring(1).toLowerCase();
            tempEarningSource.setName(sourceName);
        }

        //checks if an earningsource with the same name already exists
        if (!((EarningSourceTableAdapter)earningSourceTableAdapter).existsBasedOnName(sourceName)) {
            earningSourceTableAdapter.addNewRecord(tempEarningSource);
        } else {
            tempEarningSource = (EarningSource) earningSourceTableAdapter.findOneRecord(String.valueOf(sourceList.size()));
        }

        tempEarning.setEarningSource(tempEarningSource);


        try {
            earnList = earningTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tempEarning.setEarningID(String.valueOf(earnList.size()+1+tempEarnings.size()));
        tempEarning.setAmount((this.valueTextField.getText().isEmpty()?0.0:Double.parseDouble(this.valueTextField.getText())));
        tempEarning.setRatePerHour((this.rphTextField.getText().isEmpty()?0.0:Double.parseDouble(this.rphTextField.getText())));

        if (this.startDatePicker.getValue() != null) {
            tempEarning.setStartDate(Date.valueOf(this.startDatePicker.getValue()));
        } else {
            tempEarning.setStartDate(null); // Explicitly setting it to null if no date is selected
        }

        if (this.endDatePicker.getValue() != null) {
            tempEarning.setEndDate(Date.valueOf(this.endDatePicker.getValue()));
        } else {
            tempEarning.setEndDate(null); // Explicitly setting it to null if no date is selected
        }

        tempEarning.setEmployee(tempEmployee);

        tempEarnings.add(tempEarning);

        earningsTableView.setItems(tempEarnings);
    }

    @FXML
    public void addDeduction() throws SQLException {
        Employee tempEmployee = new Employee();
        UserAccount account = new UserAccount();
        tempEmployee.setID(this.id.getText());
        tempEmployee.setFullName(this.fullName.getText());
        tempEmployee.setCity(this.city.getText());
        tempEmployee.setProvince(this.province.getText());
        tempEmployee.setPostalCode(this.postalCode.getText());
        tempEmployee.setPhone(this.phone.getText());
        tempEmployee.setSIN(this.SIN.getText());
        tempEmployee.setMartialStatus(this.martialStatus.getText());
        tempEmployee.setJobName(this.jobName.getText());
        tempEmployee.setSkillCode(this.skillCode.getText());
        tempEmployee.setDOB(Date.valueOf(this.DOB.getValue()));
        tempEmployee.setDOH(Date.valueOf(this.DOH.getValue()));
        tempEmployee.setDOLP(Date.valueOf(this.DOLP.getValue()));
        tempEmployee.setPayType(this.payTypeBox.getValue());
        tempEmployee.setWorkHours(Double.parseDouble(this.workingText.getText()) == 0.0? 0.0 : Double.parseDouble(this.workingText.getText()));
        tempEmployee.setEarningStatementType(statementTypeBox.getValue());
        tempEmployee.setExempt(exemptCheckBox.isSelected());
        tempEmployee.setUserAccount(account);

        if (!tempEmployees.contains(tempEmployee)) {
            tempEmployees.add(tempEmployee);
        }

        DeductionSource tempDeductionSource = new DeductionSource();

        Deduction tempDeduction = new Deduction();

        //getting the amount of deductionSources and earnings in the db to create appropriate code/earningID for next source
        List<Object> sourceList, deductList;
        try {
            sourceList = deductionTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tempDeductionSource.setCode(String.valueOf(sourceList.size()+1));

        String sourceName = this.deductionBox.getValue(); // Get the value from the ComboBox
        if (sourceName != null && !sourceName.isEmpty()) {
            // Capitalize the first letter and make the rest lowercase
            sourceName = sourceName.substring(0, 1).toUpperCase() + sourceName.substring(1).toLowerCase();
            tempDeductionSource.setName(sourceName);
        }

        //checks if a deductionsource with the same name already exists
        if (!((DeductionSourceTableAdapter)deductionSourceTableAdapter).existsBasedOnName(sourceName)) {
            deductionSourceTableAdapter.addNewRecord(tempDeductionSource);
        } else {
            tempDeductionSource = (DeductionSource) deductionSourceTableAdapter.findOneRecord(String.valueOf(sourceList.size()));
        }

        tempDeduction.setDeductionSource(tempDeductionSource);


        try {
            deductList = deductionTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tempDeduction.setDeductionID(String.valueOf(deductList.size()+1+tempEarnings.size()));
        tempDeduction.setAmount((this.valueTextField2.getText().isEmpty()?0.0:Double.parseDouble(this.valueTextField2.getText())));
        tempDeduction.setPercentOfEarnings((this.poeTextField.getText().isEmpty()?0.0:Double.parseDouble(this.poeTextField.getText())));
        tempDeduction.setUpperLimit((this.upperTextField.getText().isEmpty()?0.0:Double.parseDouble(this.upperTextField.getText())));

        if (this.startDatePicker2.getValue() != null) {
            tempDeduction.setStartDate(Date.valueOf(this.startDatePicker2.getValue()));
        } else {
            tempDeduction.setStartDate(null); // Explicitly setting it to null if no date is selected
        }

        if (this.stopDatePicker.getValue() != null) {
            tempDeduction.setStopDate(Date.valueOf(this.stopDatePicker.getValue()));
        } else {
            tempDeduction.setStopDate(null); // Explicitly setting it to null if no date is selected
        }

        tempDeduction.setEmployee(tempEmployee);

        tempDeductions.add(tempDeduction);

        deductionsTableView.setItems(tempDeductions);
    }
 
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tempEmployees.clear();
        tempEarnings.clear();
        tempDeductions.clear();

        ObservableList<String> payTypes = FXCollections.observableArrayList(
                "Hourly With Card",
                "Hourly Without Card",
                "Salaried"
        );
        payTypeBox.setItems(payTypes);
        ObservableList<String> statementTypes = FXCollections.observableArrayList(
                "Weekly",
                "Bi-Weekly",
                "Semi-Monthly",
                "Monthly",
                "Special"
        );
        statementTypeBox.setItems(statementTypes);

        earningBox.setOnShowing(event -> {

            List<Object> objEarningSources;
            List<EarningSource> earningSources = new ArrayList<>();
            try {
                objEarningSources = earningSourceTableAdapter.getAllRecords();
                for(Object e : objEarningSources){
                    earningSources.add((EarningSource) e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> items = FXCollections.observableArrayList(
                    earningSources.stream().map(EarningSource::getName).collect(Collectors.toList()));
            earningBox.setItems(items);
        });

        deductionBox.setOnShowing(event -> {
            List<Object> objDeductionSources;
            List<DeductionSource> deductionSources = new ArrayList<>();
            try {
                objDeductionSources = deductionSourceTableAdapter.getAllRecords();
                for(Object ded : objDeductionSources){
                    deductionSources.add((DeductionSource) ded);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> items = FXCollections.observableArrayList(
                    deductionSources.stream().map(DeductionSource::getName).collect(Collectors.toList()));
            deductionBox.setItems(items);
        });

        earningColumn.setCellValueFactory(cellData -> {
            Earning earning = cellData.getValue();
            String earningSourceCode = earning.getEarningSource().getCode();
            String sourceName;
            try {
                sourceName = ((EarningSource)earningSourceTableAdapter.findOneRecord(earningSourceCode)).getName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new ReadOnlyStringWrapper(sourceName);
        });

        earningColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Wage", "Commission", "Bonus"));

        valueColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        rphColumn.setCellValueFactory(new PropertyValueFactory<>("ratePerHour"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Earning, Date>("startDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        deductionColumn.setCellValueFactory(cellData -> {
            Deduction deduction = cellData.getValue();
            String deductionSourceCode = deduction.getDeductionSource().getCode();
            String sourceName;
            try {
                sourceName = ((DeductionSource)deductionSourceTableAdapter.findOneRecord(deductionSourceCode)).getName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new ReadOnlyStringWrapper(sourceName);
        });

        deductionColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Income Taxes", "Insurance", "Pension", "Federal Taxes", "Provincial Taxes"));

        valueColumn2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        poeColumn.setCellValueFactory(new PropertyValueFactory<>("percentOfEarnings"));
        upperColumn.setCellValueFactory(new PropertyValueFactory<>("upperLimit"));
        startColumn2.setCellValueFactory(new PropertyValueFactory<Deduction, Date>("startDate"));
        stopColumn.setCellValueFactory(new PropertyValueFactory<>("stopDate"));
    }

}
