package dev.yuizho.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

public class CsvJdbcTest {
    @Test
    void readAllRecords() throws SQLException {
        try (var conn = new CsvDriver().connect("jdbc:classpath://demo.csv", null);) {
            try (var stmt = conn.createStatement();) {
                var resultSet = stmt.executeQuery("select * from demo");
                var size = 0;
                while (resultSet.next()) {
                    size += 1;
                    if (resultSet.getString(1).equals(2)) {
                        var actual = resultSet.getString(4);
                        assertEquals("Los Angeles", actual);
                    }
                }
                assertEquals(1000, size);
            }
        }
    }

    @Test
    void readFirstRecordByWhereExpression() throws SQLException {
        try (var conn = new CsvDriver().connect("jdbc:classpath://demo.csv", null);) {
            try (var stmt = conn.createStatement();) {
                var resultSet = stmt.executeQuery("select * from demo where id = 1");
                var size = 0;
                while (resultSet.next()) {
                    size += 1;
                    if (resultSet.getString(1).equals(1)) {
                        var actual = resultSet.getString(2);
                        assertEquals("John Doe", actual);
                    }
                }
                assertEquals(1, size);
            }
        }
    }

    @Test
    void readRecordsByWhereExpression() throws SQLException {
        try (var conn = new CsvDriver().connect("jdbc:classpath://demo.csv", null);) {
            try (var stmt = conn.createStatement();) {
              var resultSet = stmt.executeQuery("select * from demo where city = 'Independence'");
                var size = 0;
                var actualIds = new ArrayList<>();
                while (resultSet.next()) {
                    size += 1;
                    actualIds.add(resultSet.getString(1));
                }
                assertEquals(2, size);
                assertAll(
                        () -> assertEquals("191", actualIds.get(0)),
                        () -> assertEquals("260", actualIds.get(1))
                );
            }
        }
    }

    @Test
    void readAllRecordsInFolder(@TempDir Path tempDir) throws SQLException, URISyntaxException, IOException {
        var testFilePath = Path.of(CsvJdbcTest.class.getClassLoader().getResource("demo.csv").toURI());
        var destTempFilePath = tempDir.resolve("demo.csv");
        Files.copy(testFilePath, destTempFilePath);

        try (var conn = new CsvDriver().connect("jdbc:file://" + testFilePath, null);) {
            try (var stmt = conn.createStatement();) {
                var resultSet = stmt.executeQuery("select * from demo");
                var size = 0;
                while (resultSet.next()) {
                    size += 1;
                    if (resultSet.getString(1).equals(2)) {
                        var actual = resultSet.getString(4);
                        assertEquals("Los Angeles", actual);
                    }
                }
                assertEquals(1000, size);
            }
        }
    }
}
