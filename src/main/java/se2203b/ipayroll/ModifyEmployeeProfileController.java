/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.ipayroll;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
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
public class ModifyEmployeeProfileController implements Initializable {

    @FXML
    private TabPane tb;
    @FXML
    private ComboBox<String> id;
    @FXML
    private TextField fullName, city, province, phone, SIN, postalCode,
            martialStatus, jobName, skillCode;
    @FXML
    private DatePicker DOB, DOH, DOLP;
    @FXML
    Button cancelBtn, saveBtn;
    /******************/
    @FXML
    Button addBtn;
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
    private Button RemoveEarning;

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
    @FXML
    private Button removeDBtn;
    @FXML
    private Button addDBtn;

    private DataStore userAccountTable;
    private UserAccount userAccount;
    private DataStore employeeTable;
    private Employee employee = null;
    private DataStore earningTableAdapter;
    private DataStore earningSourceTableAdapter;
    private DataStore deductionTableAdapter;
    private DataStore deductionSourceTableAdapter;

    private IPayrollController iPAYROLLController;

    final ObservableList<String> data = FXCollections.observableArrayList();
    private final ObservableList<Earning> tempEarnings = FXCollections.observableArrayList();
    private final ObservableList<Employee> tempEmployees = FXCollections.observableArrayList();
    private final ObservableList<Deduction> tempDeductions = FXCollections.observableArrayList();

    public void setDataStore(DataStore account, DataStore profile, DataStore earnTable, DataStore eSourceTable, DataStore deductTable, DataStore dSourceTable) {
        userAccountTable = account;
        employeeTable = profile;
        earningTableAdapter = earnTable;
        earningSourceTableAdapter = eSourceTable;
        deductionTableAdapter = deductTable;
        deductionSourceTableAdapter = dSourceTable;
        buildData();
    }

    public void setIPayrollController(IPayrollController controller) {
        iPAYROLLController = controller;
    }


    @FXML
    public void getProfile() {
        try {
            employee = (Employee) employeeTable.findOneRecord(this.id.getValue());
            this.fullName.setText(employee.getFullName());
            this.city.setText(employee.getCity());
            this.province.setText(employee.getProvince());
            this.phone.setText(employee.getPhone());
            this.postalCode.setText(employee.getPostalCode());
            this.SIN.setText(employee.getSIN());
            this.martialStatus.setText(employee.getMartialStatus());
            this.jobName.setText(employee.getJobName());
            this.skillCode.setText(employee.getSkillCode());
            Date utilDOB = new Date(employee.getDOB().getTime());
            this.DOB.setValue(utilDOB.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date utilDOH = new Date(employee.getDOH().getTime());
            this.DOH.setValue(utilDOH.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date utilDOLP = new Date(employee.getDOLP().getTime());
            this.DOLP.setValue(utilDOLP.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            this.payTypeBox.setValue(employee.getPayType());
            this.workingText.setText(String.valueOf(employee.getWorkHours()));
            this.statementTypeBox.setValue(employee.getEarningStatementType());
            this.exemptCheckBox.setSelected(employee.getExempt());

            updateEarningsTableView(employee);
            updateDeductionsTableView(employee);

            userAccount = (UserAccount) userAccountTable.findOneRecord(employee.getUserAccount().getUserAccountName());

        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void update() {
        try {
            Employee oneEmployee = new Employee();
            oneEmployee.setID(employee.getID());
            oneEmployee.setFullName(this.fullName.getText());
            oneEmployee.setCity(this.city.getText());
            oneEmployee.setProvince(this.province.getText());
            oneEmployee.setPostalCode(this.postalCode.getText());
            oneEmployee.setPhone(this.phone.getText());
            oneEmployee.setSIN(this.SIN.getText());
            oneEmployee.setMartialStatus(this.martialStatus.getText());
            oneEmployee.setJobName(this.jobName.getText());
            oneEmployee.setSkillCode(this.skillCode.getText());
            oneEmployee.setDOB(java.sql.Date.valueOf(this.DOB.getValue()));
            oneEmployee.setDOH(java.sql.Date.valueOf(this.DOH.getValue()));
            oneEmployee.setDOLP(java.sql.Date.valueOf(this.DOLP.getValue()));
            oneEmployee.setPayType(this.payTypeBox.getValue());
            oneEmployee.setWorkHours(Double.parseDouble(this.workingText.getText()));
            oneEmployee.setEarningStatementType(statementTypeBox.getValue());
            oneEmployee.setExempt(exemptCheckBox.isSelected());

            oneEmployee.setUserAccount(userAccount);

            employeeTable.updateRecord(oneEmployee);
        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void delete() {
        // Check if there is a related user account
        if (employee.getUserAccount().getUserAccountName() == null) {
            try {
                earningTableAdapter.deleteRecords(employee);
                deductionTableAdapter.deleteRecords(employee);
                employeeTable.deleteOneRecord(employee.getID());
            } catch (SQLException ex) {
                iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
            }
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        } else {
            iPAYROLLController.displayAlert("Please delete the associated user account first");
        }
    }

    public void buildData() {
        try {
            data.addAll(employeeTable.getKeys());
        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }
    }

    @FXML
    public void add() throws SQLException {
        Earning earning = new Earning();
        EarningSource earningSource = new EarningSource();
        Employee employee = (Employee) employeeTable.findOneRecord(id.getValue());

        //getting the amount of earningSources and earnings in the db to create appropriate code/earningID for next source
        List<Object> sourceList, earnList;
        try {
            sourceList = earningSourceTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        earningSource.setCode(String.valueOf(sourceList.size()+1));

        earningSource.setName((String)this.earningBox.getValue());
        //checks if an earningsource with the same name already exists, and handles that case accordingly
        boolean exists = false;
        for(Object i : sourceList){
            if(Objects.equals(earningSource.getName().toLowerCase(), ((EarningSource) i).getName().toLowerCase())){
                exists = true;
                earning.setEarningSource((EarningSource) i);
                break;
            }
        }
        if(!exists) {
            earning.setEarningSource(earningSource);
        }

        try {
            earnList = earningTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        earning.setEarningID(String.valueOf(earnList.size()+1));

        earning.setAmount((this.valueTextField.getText().isEmpty()?0.0:Double.parseDouble(this.valueTextField.getText())));
        earning.setRatePerHour((this.rphTextField.getText().isEmpty()?0.0:Double.parseDouble(this.rphTextField.getText())));

        if (this.startDatePicker.getValue() != null) {
            earning.setStartDate(java.sql.Date.valueOf(this.startDatePicker.getValue()));
        } else {
            earning.setStartDate(null); // Explicitly setting it to null if no date is selected
        }

        if (this.endDatePicker.getValue() != null) {
            earning.setEndDate(java.sql.Date.valueOf(this.endDatePicker.getValue()));
        } else {
            earning.setEndDate(null); // Explicitly setting it to null if no date is selected
        }
        earning.setEmployee(employee);

        if(!exists) {
            earningSourceTableAdapter.addNewRecord(earningSource);
        }
        earningTableAdapter.addNewRecord(earning);

        updateEarningsTableView(employee);
    }

    private void updateEarningsTableView(Employee employee) throws SQLException {
        // Clear existing items
        earningsTableView.getItems().clear();

        // Fetch earnings for the selected employee
        List<Object> earningsObjectsList = earningTableAdapter.getAllRecords(employee);
        List<Earning> earningsList = new ArrayList<>();
        for (Object object : earningsObjectsList) {
            if (object instanceof Earning) {
                earningsList.add((Earning) object);
            }
        }

        // Convert to ObservableList and update TableView
        ObservableList<Earning> earningsObservableList = FXCollections.observableArrayList(earningsList);
        earningsTableView.setItems(earningsObservableList);
    }

    private void updateDeductionsTableView(Employee employee) throws SQLException {
        // Clear existing items
        deductionsTableView.getItems().clear();

        // Fetch deductions for the selected employee
        List<Object> deductionsObjectsList = deductionTableAdapter.getAllRecords(employee);
        List<Deduction> deductionsList = new ArrayList<>();
        for (Object object : deductionsObjectsList) {
            if (object instanceof Deduction) {
                deductionsList.add((Deduction) object);
            }
        }

        // Convert to ObservableList and update TableView
        ObservableList<Deduction> deductionsObservableList = FXCollections.observableArrayList(deductionsList);
        deductionsTableView.setItems(deductionsObservableList);
    }

    @FXML
    public void deleteEarning() {
        Earning selectedEarning = earningsTableView.getSelectionModel().getSelectedItem();
        if (selectedEarning != null) {
            try {
                // Delete the selected Earning using the EarningTableAdapter
                earningTableAdapter.deleteOneRecord(selectedEarning.getEarningID());

                // Refresh the TableView
                updateEarningsTableView((Employee) employeeTable.findOneRecord(id.getValue()));

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    @FXML
    public void deleteDeduction(){
        Deduction selectedDeduction = deductionsTableView.getSelectionModel().getSelectedItem();
        if (selectedDeduction != null) {
            try {
                // Delete the selected Deduction using the EarningTableAdapter
                deductionTableAdapter.deleteOneRecord(selectedDeduction.getDeductionID());

                // Refresh the TableView
                updateDeductionsTableView((Employee) employeeTable.findOneRecord(id.getValue()));

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addDeduction() throws SQLException {
        Deduction deduction = new Deduction();
        DeductionSource deductionSource = new DeductionSource();
        Employee employee = (Employee) employeeTable.findOneRecord(id.getValue());

        //getting the amount of deductionSources and earnings in the db to create appropriate code/deductionID for next source
        List<Object> sourceList, deductList;
        try {
            sourceList = deductionSourceTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        deductionSource.setCode(String.valueOf(sourceList.size()+1));

        deductionSource.setName((String)this.deductionBox.getValue());
        //checks if a deductionsource with the same name already exists, and handles that case accordingly
        boolean exists = false;
        for(Object i : sourceList){
            if(Objects.equals(deductionSource.getName().toLowerCase(), ((DeductionSource) i).getName().toLowerCase())){
                exists = true;
                deduction.setDeductionSource((DeductionSource) i);
                break;
            }
        }
        if(!exists) {
            deduction.setDeductionSource(deductionSource);
        }

        try {
            deductList = deductionTableAdapter.getAllRecords();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        deduction.setDeductionID(String.valueOf(deductList.size()+1));

        deduction.setAmount((this.valueTextField2.getText().isEmpty()?0.0:Double.parseDouble(this.valueTextField2.getText())));
        deduction.setPercentOfEarnings((this.poeTextField.getText().isEmpty()?0.0:Double.parseDouble(this.poeTextField.getText())));
        deduction.setUpperLimit((this.upperTextField.getText().isEmpty()?0.0:Double.parseDouble(this.upperTextField.getText())));

        if (this.startDatePicker2.getValue() != null) {
            deduction.setStartDate(java.sql.Date.valueOf(this.startDatePicker2.getValue()));
        } else {
            deduction.setStartDate(null); // Explicitly setting it to null if no date is selected
        }

        if (this.stopDatePicker.getValue() != null) {
            deduction.setStopDate(java.sql.Date.valueOf(this.stopDatePicker.getValue()));
        } else {
            deduction.setStopDate(null); // Explicitly setting it to null if no date is selected
        }
        deduction.setEmployee(employee);

        if(!exists) {
            deductionSourceTableAdapter.addNewRecord(deductionSource);
        }
        deductionTableAdapter.addNewRecord(deduction);

        updateDeductionsTableView(employee);
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
        id.setItems(data);

        EarningSource Wage = new EarningSource();
        EarningSource Commission = new EarningSource();
        EarningSource Bonus = new EarningSource();
        Wage.setCode("1");
        Wage.setName("Wage");
        Commission.setCode("2");
        Commission.setName("Commission");
        Bonus.setCode("3");
        Bonus.setName("Bonus");

        DeductionSource incomeTaxes = new DeductionSource();
        DeductionSource insurance = new DeductionSource();
        DeductionSource pension = new DeductionSource();
        DeductionSource federalTaxes = new DeductionSource();
        DeductionSource provincialTaxes = new DeductionSource();
        incomeTaxes.setCode("1");
        incomeTaxes.setName("Income Taxes");
        insurance.setCode("2");
        insurance.setName("Insurance");
        pension.setCode("3");
        pension.setName("Pension");
        federalTaxes.setCode("4");
        federalTaxes.setName("Federal Taxes");
        provincialTaxes.setCode("5");
        provincialTaxes.setName("Provincial Taxes");

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
            try {
                earningSourceTableAdapter.addNewRecord(Wage);
                earningSourceTableAdapter.addNewRecord(Commission);
                earningSourceTableAdapter.addNewRecord(Bonus);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
            try {
                deductionSourceTableAdapter.addNewRecord(incomeTaxes);
                deductionSourceTableAdapter.addNewRecord(insurance);
                deductionSourceTableAdapter.addNewRecord(pension);
                deductionSourceTableAdapter.addNewRecord(federalTaxes);
                deductionSourceTableAdapter.addNewRecord(provincialTaxes);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            List<Object> objDeductionSources;
            List<DeductionSource> deductionSources = new ArrayList<>();
            try {
                objDeductionSources = deductionSourceTableAdapter.getAllRecords();
                for(Object e : objDeductionSources){
                    deductionSources.add((DeductionSource) e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> items = FXCollections.observableArrayList(
                    deductionSources.stream().map(DeductionSource::getName).collect(Collectors.toList()));
            deductionBox.setItems(items);
        });

        earningColumn.setCellValueFactory(cellData ->
        {
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
        earningColumn.setCellFactory(ComboBoxTableCell.<Earning, String>forTableColumn("Wage", "Commission", "Bonus"));


        List<EarningSource> l = new ArrayList<>();
        l.add(Wage);
        l.add(Commission);
        l.add(Bonus);

        earningColumn.setOnEditCommit(event -> {
            Earning editedEarning = event.getRowValue();
            String selectedEarningSourceName = event.getNewValue();
            try {
                for(EarningSource i : l){
                    if(Objects.equals(selectedEarningSourceName.toLowerCase(), i.getName().toLowerCase())){
                        editedEarning.setEarningSource(i);
                    }
                }
                earningTableAdapter.updateRecord(editedEarning);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });


        valueColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        valueColumn.setCellFactory(TextFieldTableCell.<Earning, Double>forTableColumn(new DoubleStringConverter()));

        valueColumn.setOnEditCommit(event -> {
            Earning editedEarning = event.getRowValue();
            double newValue = event.getNewValue();
            editedEarning.setAmount(newValue);
            try {
                earningTableAdapter.updateRecord(editedEarning);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        rphColumn.setCellValueFactory(new PropertyValueFactory<>("ratePerHour"));
        rphColumn.setCellFactory(TextFieldTableCell.<Earning, Double>forTableColumn(new DoubleStringConverter()));
        rphColumn.setOnEditCommit(event -> {
            Earning editedEarning = event.getRowValue();
            double newRPH = event.getNewValue();
            editedEarning.setRatePerHour(newRPH);
            try {
                earningTableAdapter.updateRecord(editedEarning);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        startColumn.setCellValueFactory(new PropertyValueFactory<Earning, Date>("startDate"));
        startColumn.setCellFactory(TextFieldTableCell.<Earning, Date>forTableColumn(new DateStringConverter()));
        startColumn.setOnEditCommit(event -> {
            Earning editedEarning = event.getRowValue();
            Date newStart = event.getNewValue();
            editedEarning.setStartDate((java.sql.Date)newStart);
            try {
                earningTableAdapter.updateRecord(editedEarning);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        endColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endColumn.setCellFactory(TextFieldTableCell.<Earning, Date>forTableColumn(new DateStringConverter()));
        endColumn.setOnEditCommit(event -> {
            Earning editedEarning = event.getRowValue();
            Date newEnd = event.getNewValue();
            editedEarning.setEndDate((java.sql.Date)newEnd);
            try {
                earningTableAdapter.updateRecord(editedEarning);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        deductionColumn.setCellValueFactory(cellData ->
        {
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
        deductionColumn.setCellFactory(ComboBoxTableCell.<Deduction, String>forTableColumn("Income Taxes", "Insurance", "Pension", "Federal Taxes", "Provincial Taxes"));


        List<DeductionSource> d = new ArrayList<>();
        d.add(incomeTaxes);
        d.add(insurance);
        d.add(pension);
        d.add(federalTaxes);
        d.add(provincialTaxes);

        deductionColumn.setOnEditCommit(event -> {
            Deduction editedDeduction = event.getRowValue();
            String selectedDeductionSourceName = event.getNewValue();
            try {
                for(DeductionSource i : d){
                    if(Objects.equals(selectedDeductionSourceName.toLowerCase(), i.getName().toLowerCase())){
                        editedDeduction.setDeductionSource(i);
                    }
                }
                deductionTableAdapter.updateRecord(editedDeduction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });


        valueColumn2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        valueColumn2.setCellFactory(TextFieldTableCell.<Deduction, Double>forTableColumn(new DoubleStringConverter()));
        valueColumn2.setOnEditCommit(event -> {
            Deduction editedDeduction = event.getRowValue();
            double newValue = event.getNewValue();
            editedDeduction.setAmount(newValue);
            try {
                deductionTableAdapter.updateRecord(editedDeduction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        poeColumn.setCellValueFactory(new PropertyValueFactory<>("percentOfEarnings"));
        poeColumn.setCellFactory(TextFieldTableCell.<Deduction, Double>forTableColumn(new DoubleStringConverter()));
        poeColumn.setOnEditCommit(event -> {
            Deduction editedDeduction = event.getRowValue();
            double newPOE = event.getNewValue();
            editedDeduction.setPercentOfEarnings(newPOE);
            try {
                earningTableAdapter.updateRecord(editedDeduction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        upperColumn.setCellValueFactory(new PropertyValueFactory<>("upperLimit"));
        upperColumn.setCellFactory(TextFieldTableCell.<Deduction, Double>forTableColumn(new DoubleStringConverter()));
        upperColumn.setOnEditCommit(event -> {
            Deduction editedDeduction = event.getRowValue();
            double newUpper = event.getNewValue();
            editedDeduction.setUpperLimit(newUpper);
            try {
                deductionTableAdapter.updateRecord(editedDeduction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        startColumn2.setCellValueFactory(new PropertyValueFactory<Deduction, Date>("startDate"));
        startColumn2.setCellFactory(TextFieldTableCell.<Deduction, Date>forTableColumn(new DateStringConverter()));
        startColumn2.setOnEditCommit(event -> {
            Deduction editedDeduction = event.getRowValue();
            Date newStart = event.getNewValue();
            editedDeduction.setStartDate((java.sql.Date)newStart);
            try {
                deductionTableAdapter.updateRecord(editedDeduction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        stopColumn.setCellValueFactory(new PropertyValueFactory<>("stopDate"));
        stopColumn.setCellFactory(TextFieldTableCell.<Deduction, Date>forTableColumn(new DateStringConverter()));
        stopColumn.setOnEditCommit(event -> {
            Deduction editedDeduction = event.getRowValue();
            Date newStop = event.getNewValue();
            editedDeduction.setStopDate((java.sql.Date)newStop);
            try {
                deductionTableAdapter.updateRecord(editedDeduction);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
