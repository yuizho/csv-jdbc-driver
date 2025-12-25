package dev.yuizho.jdbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

public class CsvJdbcTest {
    @Test
    void readFirstRecordsOfDemoCSV() throws SQLException {
        try (var conn = new CsvDriver().connect("jdbc://demo.csv", null);) {
            try (var stmt = conn.createStatement();) {
                var resultSet = stmt.executeQuery("select * from demo");
                resultSet.next();
                var actual = resultSet.getString(2);

                assertEquals("John Doe", actual);
            }
        }
    }

    @Test
    void readSecondRecordOfDemoCSV() throws SQLException {
        try (var conn = new CsvDriver().connect("jdbc://demo.csv", null);) {
            try (var stmt = conn.createStatement();) {
                var resultSet = stmt.executeQuery("select * from demo");
                while (resultSet.next()) {
                    if (resultSet.getString(1).equals(2)) {
                        var actual = resultSet.getString(4);
                        assertEquals("Los Angeles", actual);
                    }
                }
            }
        }
    }
}
