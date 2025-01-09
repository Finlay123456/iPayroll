package se2203b.ipayroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EarningTableAdapter implements DataStore {

    private Connection connection;
    private String DB_URL = "jdbc:derby:iPAYROLLDB";

    // Constructor to handle the database connection
    public EarningTableAdapter(Boolean reset) throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Attempt to drop the table if it exists
                stmt.execute("DROP TABLE Earning");
            } catch (SQLException ex) {
                // Table doesn't exist or couldn't be dropped; no action needed
            }
        }
        // Attempt to create the table
        try {
            stmt.execute("CREATE TABLE Earning ("
                    + "earningID VARCHAR(25) PRIMARY KEY, "
                    + "amount DOUBLE, "
                    + "ratePerHour DOUBLE, "
                    + "startDate DATE, "
                    + "endDate DATE, "
                    + "earningSource VARCHAR(9) REFERENCES EarningSource(CODE), "
                    + "employee VARCHAR(9) REFERENCES Employee(id)"
                    + ")");
        } catch (SQLException ex) {
            // Table already exists or couldn't be created; no action needed
        }
    }


    @Override
    public void addNewRecord(Object data) throws SQLException {
        Earning earning = (Earning) data;
        String SQL = "INSERT INTO Earning (earningID, amount, ratePerHour, startDate, endDate, earningSource, employee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, earning.getEarningID());
            pstmt.setDouble(2, earning.getAmount());
            pstmt.setDouble(3, earning.getRatePerHour());
            pstmt.setDate(4, earning.getStartDate() != null ? new Date(earning.getStartDate().getTime()) : null);
            pstmt.setDate(5, earning.getEndDate() != null ? new Date(earning.getEndDate().getTime()) : null);
            pstmt.setString(6, earning.getEarningSource().getCode());
            pstmt.setString(7, earning.getEmployee().getID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding new earning record", e);
        }
    }

    public void updateRecord(Object data) throws SQLException {
        Earning earning = (Earning) data;
        String SQL = "UPDATE Earning SET amount = ?, ratePerHour = ?, startDate = ?, endDate = ?, earningSource = ?, employee = ? " +
                "WHERE earningID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDouble(1, earning.getAmount());
            pstmt.setDouble(2, earning.getRatePerHour());
            pstmt.setDate(3, earning.getStartDate() != null ? new Date(earning.getStartDate().getTime()) : null);
            pstmt.setDate(4, earning.getEndDate() != null ? new Date(earning.getEndDate().getTime()) : null);
            pstmt.setString(5, earning.getEarningSource().getCode());
            pstmt.setString(6, earning.getEmployee().getID());
            pstmt.setString(7, earning.getEarningID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating earning record", e);
        }
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        Earning earning = null;
        String SQL = "SELECT * FROM Earning WHERE earningID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, key);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    earning = new Earning();
                    earning.setEarningID(rs.getString("earningID"));
                    earning.setAmount(rs.getDouble("amount"));
                    earning.setRatePerHour(rs.getDouble("ratePerHour"));
                    earning.setStartDate(rs.getDate("startDate"));
                    earning.setEndDate(rs.getDate("endDate"));

                    Employee employee = new Employee();
                    employee.setID(rs.getString("Employee"));
                    earning.setEmployee(employee);

                    EarningSource earningSource = new EarningSource();
                    earningSource.setCode(rs.getString("EarningSource"));
                    earning.setEarningSource(earningSource);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding earning record", e);
        }
        return earning;
    }

    @Override
    public Object findOneRecord(Object employee) throws SQLException {
        return null;
    }

    @Override
    public void deleteOneRecord(String key) throws SQLException {
        String SQL = "DELETE FROM Earning WHERE earningID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting earning record", e);
        }
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        String IdToDelete = ((Employee)referencedObject).getID();
        String SQL = "DELETE FROM Earning WHERE EMPLOYEE = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, IdToDelete);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting earning record", e);
        }
    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        String SQL = "SELECT earningID FROM Earning";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                keys.add(rs.getString("earningID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving earning keys", e);
        }
        return keys;
    }


    public List<Object> getAllRecords() throws SQLException {
        List<Object> earnings = new ArrayList<>();
        String SQL = "SELECT * FROM Earning";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            ResultSet result = pstmt.executeQuery()) {

            while (result.next()) {
                Earning earning = new Earning();
                earning.setEarningID(result.getString("EarningID"));
                earning.setAmount(result.getDouble("Amount"));
                earning.setRatePerHour(result.getDouble("RatePerHour"));
                earning.setStartDate(result.getDate("StartDate"));
                earning.setEndDate(result.getDate("EndDate"));

                Employee employee = new Employee();
                employee.setID(result.getString("Employee"));
                earning.setEmployee(employee);

                EarningSource earningSource = new EarningSource();
                earningSource.setCode(result.getString("EarningSource"));
                earning.setEarningSource(earningSource);

                earnings.add(earning);
            }
            connection.close();
            return earnings;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> getAllRecords(Object employee) throws SQLException {
        List<Object> earnings = new ArrayList<>();
        // Adjust the SQL to perform a JOIN with the EarningSource table
        String SQL = "SELECT E.*, ES.code AS sourceCode, ES.SOURCENAME AS sourceName FROM Earning E JOIN EarningSource ES ON E.earningSource = ES.code WHERE E.employee = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, ((Employee)employee).getID());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Earning earning = new Earning();
                    earning.setEarningID(rs.getString("earningID"));
                    earning.setAmount(rs.getDouble("amount"));
                    earning.setRatePerHour(rs.getDouble("ratePerHour"));
                    earning.setStartDate(rs.getDate("startDate"));
                    earning.setEndDate(rs.getDate("endDate"));
                    // Directly set EarningSource using fetched data
                    EarningSource earningSource = new EarningSource();
                    earningSource.setCode(rs.getString("sourceCode"));
                    earningSource.setName(rs.getString("sourceName"));
                    earning.setEarningSource(earningSource);
                    earning.setEmployee((Employee) employee);
                    earnings.add(earning);
                }
            }
        }
        return earnings;
    }
}