package se2203b.ipayroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeductionTableAdapter implements DataStore{
    private Connection connection;
    private String DB_URL = "jdbc:derby:iPAYROLLDB";

    // Constructor to handle the database connection
    public DeductionTableAdapter(Boolean reset) throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Attempt to drop the table if it exists
                stmt.execute("DROP TABLE DEDUCTION");
            } catch (SQLException ex) {
                // Table doesn't exist or couldn't be dropped; no action needed
            }
        }
        // Attempt to create the table
        try {
            stmt.execute("CREATE TABLE DEDUCTION ("
                    + "deductionID VARCHAR(9) PRIMARY KEY, "
                    + "amount DOUBLE, "
                    + "percentOfEarnings DOUBLE, "
                    + "startDate DATE, "
                    + "stopDate DATE, "
                    + "deductionSource VARCHAR(9) REFERENCES DEDUCTIONSOURCE(CODE), "
                    + "employee VARCHAR(9) REFERENCES Employee(id)"
                    + ")");
        } catch (SQLException ex) {
            // Table already exists or couldn't be created; no action needed
        }
    }


    @Override
    public void addNewRecord(Object data) throws SQLException {
        Deduction deduction = (Deduction) data;
        String SQL = "INSERT INTO DEDUCTION (DEDUCTIONID, amount, PERCENTOFEARNINGS, startDate, STOPDATE, DEDUCTIONSOURCE, employee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, deduction.getDeductionID());
            pstmt.setDouble(2, deduction.getAmount());
            pstmt.setDouble(3, deduction.getPercentOfEarnings());
            pstmt.setDate(4, deduction.getStartDate() != null ? new Date(deduction.getStartDate().getTime()) : null);
            pstmt.setDate(5, deduction.getStopDate() != null ? new Date(deduction.getStopDate().getTime()) : null);
            pstmt.setString(6, deduction.getDeductionSource().getCode());
            pstmt.setString(7, deduction.getEmployee().getID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding new deduction record", e);
        }
    }

    public void updateRecord(Object data) throws SQLException {
        Deduction deduction = (Deduction) data;
        String SQL = "UPDATE DEDUCTION SET amount = ?, PERCENTOFEARNINGS = ?, startDate = ?, STOPDATE = ?, DEDUCTIONSOURCE = ?, employee = ? " +
                "WHERE DEDUCTIONID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDouble(1, deduction.getAmount());
            pstmt.setDouble(2, deduction.getPercentOfEarnings());
            pstmt.setDate(3, deduction.getStartDate() != null ? new Date(deduction.getStartDate().getTime()) : null);
            pstmt.setDate(4, deduction.getStopDate() != null ? new Date(deduction.getStopDate().getTime()) : null);
            pstmt.setString(5, deduction.getDeductionSource().getCode());
            pstmt.setString(6, deduction.getEmployee().getID());
            pstmt.setString(7, deduction.getDeductionID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating earning record", e);
        }
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        Deduction deduction = null;
        String SQL = "SELECT * FROM DEDUCTION WHERE DEDUCTIONID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, key);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    deduction = new Deduction();
                    deduction.setDeductionID(rs.getString("deductionID"));
                    deduction.setAmount(rs.getDouble("amount"));
                    deduction.setPercentOfEarnings(rs.getDouble("percentOfEarnings"));
                    deduction.setStartDate(rs.getDate("startDate"));
                    deduction.setStopDate(rs.getDate("stopDate"));

                    Employee employee = new Employee();
                    employee.setID(rs.getString("Employee"));
                    deduction.setEmployee(employee);

                    DeductionSource deductionSource = new DeductionSource();
                    deductionSource.setCode(rs.getString("DeductionSource"));
                    deduction.setDeductionSource(deductionSource);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding deduction record", e);
        }
        return deduction;
    }

    @Override
    public Object findOneRecord(Object employee) throws SQLException {
        return null;
    }

    @Override
    public void deleteOneRecord(String key) throws SQLException {
        String SQL = "DELETE FROM DEDUCTION WHERE DEDUCTIONID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting deduction record", e);
        }
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        String IdToDelete = ((Employee)referencedObject).getID();
        String SQL = "DELETE FROM DEDUCTION WHERE EMPLOYEE = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, IdToDelete);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting deduction record", e);
        }
    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        String SQL = "SELECT DEDUCTIONID FROM DEDUCTION";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                keys.add(rs.getString("deductionID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving deduction keys", e);
        }
        return keys;
    }


    public List<Object> getAllRecords() throws SQLException {
        List<Object> deductions = new ArrayList<>();
        String SQL = "SELECT * FROM DEDUCTION";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                Deduction deduction = new Deduction();
                deduction.setDeductionID(result.getString("DeductionID"));
                deduction.setAmount(result.getDouble("Amount"));
                deduction.setPercentOfEarnings(result.getDouble("PercentOfEarning"));
                deduction.setStartDate(result.getDate("StartDate"));
                deduction.setStopDate(result.getDate("StopDate"));

                Employee employee = new Employee();
                employee.setID(result.getString("Employee"));
                deduction.setEmployee(employee);

                DeductionSource deductionSource = new DeductionSource();
                deductionSource.setCode(result.getString("DeductionSource"));
                deduction.setDeductionSource(deductionSource);

                deductions.add(deduction);
            }
            connection.close();
            return deductions;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> getAllRecords(Object employee) throws SQLException {
        List<Object> deductions = new ArrayList<>();
        // Create an SQL to perform a JOIN with the DeductionSource table
        String SQL = "SELECT D.*, DS.code AS sourceCode, DS.SOURCENAME AS sourceName FROM DEDUCTION D JOIN DEDUCTIONSOURCE DS ON D.DEDUCTIONSOURCE = DS.code WHERE D.employee = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, ((Employee)employee).getID());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Deduction deduction = new Deduction();
                    deduction.setDeductionID(rs.getString("deductionID"));
                    deduction.setAmount(rs.getDouble("amount"));
                    deduction.setPercentOfEarnings(rs.getDouble("percentOfEarnings"));
                    deduction.setStartDate(rs.getDate("startDate"));
                    deduction.setStopDate(rs.getDate("stopDate"));
                    // Directly set EarningSource using fetched data
                    DeductionSource deductionSource = new DeductionSource();
                    deductionSource.setCode(rs.getString("sourceCode"));
                    deductionSource.setName(rs.getString("sourceName"));
                    deduction.setDeductionSource(deductionSource);
                    deduction.setEmployee((Employee) employee);
                    deductions.add(deduction);
                }
            }
        }
        return deductions;
    }
}
