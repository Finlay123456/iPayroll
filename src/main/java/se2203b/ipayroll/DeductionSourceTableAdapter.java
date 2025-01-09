package se2203b.ipayroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeductionSourceTableAdapter implements DataStore {
    private Connection connection;
    private final String DB_URL = "jdbc:derby:iPayrollDB";

    public DeductionSourceTableAdapter(Boolean reset) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            if (reset) {
                try {
                    stmt.execute("DROP TABLE DEDUCTIONSOURCE");
                } catch (SQLException ignored) {
                }
            }
            try {
                stmt.execute("CREATE TABLE DEDUCTIONSOURCE (" +
                        "CODE VARCHAR(9) NOT NULL PRIMARY KEY, " +
                        "SOURCENAME VARCHAR(50)" +
                        ")");
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        DeductionSource deductionSource = (DeductionSource) data;
        if(findOneRecord(deductionSource.getCode())==null) {
            String addSQL = "INSERT INTO DEDUCTIONSOURCE (CODE, SOURCENAME) VALUES (?, ?)";

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = connection.prepareStatement(addSQL)) {
                pstmt.setString(1, deductionSource.getCode());
                pstmt.setString(2, deductionSource.getName());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        DeductionSource source = (DeductionSource) data;
        String updateSQL = "UPDATE DeductionSource SET SOURCENAME = ? WHERE CODE = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, source.getName());
            pstmt.setString(2, source.getCode());
            pstmt.executeUpdate();
        }
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        String selectSQL = "SELECT * FROM DeductionSource WHERE CODE = ?";
        DeductionSource source = null;

        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    source = new DeductionSource();
                    source.setCode(rs.getString("CODE"));
                    source.setName(rs.getString("SOURCENAME"));
                }
            }
        }
        return source;
    }

    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        return null;
    }

    @Override
    public void deleteOneRecord(String key) throws SQLException {
        String deleteSQL = "DELETE FROM DeductionSource WHERE code = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, key);
            pstmt.executeUpdate();
        }

    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {

    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT CODE FROM DEDUCTIONSOURCE";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        return list;
    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        String sql = "SELECT * FROM DEDUCTIONSOURCE";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                DeductionSource source = new DeductionSource();
                source.setCode(result.getString("CODE"));
                source.setName(result.getString("SOURCENAME"));
                list.add(source);
            }
        }
        return list;
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }

    public boolean existsBasedOnName(String name) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        boolean exists = false;

        try {
            // Prepare a SQL statement to prevent SQL injection
            String query = "SELECT COUNT(*) AS count FROM DEDUCTIONSOURCE WHERE SOURCENAME = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If the count is greater than 0, the record exists
            if (rs.next()) {
                int count = rs.getInt("count");
                exists = count > 0;
            }
        } finally {
            // Close the connection
            if (connection != null) connection.close();
        }

        return exists;
    }
}