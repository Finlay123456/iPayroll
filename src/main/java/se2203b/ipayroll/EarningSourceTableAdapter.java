package se2203b.ipayroll;

import java.sql.*;
import java.util.*;

public class EarningSourceTableAdapter implements DataStore{

    private Connection connection;
    private String DB_URL = "jdbc:derby:iPAYROLLDB";

    // Constructor to handle the database connection
    public EarningSourceTableAdapter(Boolean reset) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement()) {
            if (reset) {
                try {
                    stmt.execute("DROP TABLE EarningSource");
                } catch (SQLException ignored) {
                }
            }
            try {
                stmt.execute("CREATE TABLE EarningSource (" +
                        "CODE VARCHAR(9) NOT NULL PRIMARY KEY, " +
                        "SOURCENAME VARCHAR(50)" +
                        ")");
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        EarningSource earningSource = (EarningSource) data;
        if(findOneRecord(earningSource.getCode())==null) {
            String sql = "INSERT INTO EarningSource (CODE, SOURCENAME) VALUES (?, ?)";

            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, earningSource.getCode());
                pstmt.setString(2, earningSource.getName());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else System.out.println("EarningSource already exists");
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        EarningSource earningSource = (EarningSource) data;
        String sql = "UPDATE EarningSource SET SOURCENAME = ? WHERE CODE = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, earningSource.getName());
            pstmt.setString(2, earningSource.getCode());
            pstmt.executeUpdate();
        }
    }
    @Override
    public Object findOneRecord(String key) throws SQLException {
        EarningSource source = null;
        String sql = "SELECT * FROM EarningSource WHERE CODE = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, key);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    source = new EarningSource();
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
        String sql = "DELETE FROM EARNINGSOURCE WHERE CODE = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
        String sql = "SELECT CODE FROM EARNINGSOURCE";

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
        String sql = "SELECT * FROM EARNINGSOURCE";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                EarningSource source = new EarningSource();
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
            String query = "SELECT COUNT(*) AS count FROM EarningSource WHERE SOURCENAME = ?";
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
