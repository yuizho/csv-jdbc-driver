package dev.yuizho.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class CsvDriver implements Driver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDriver.class);

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        LOGGER.info("CsvDriver#connect");
        if (url.startsWith(ClasspathCsvConnection.URL_PREFIX)) {
            return new ClasspathCsvConnection(url);
        } else if (url.startsWith(FileCsvConnection.URL_PREFIX)) {
            return new FileCsvConnection(url);
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            "urlは %s or %s で始まる形式で入力してください。",
                            ClasspathCsvConnection.URL_PREFIX,
                            FileCsvConnection.URL_PREFIX
                    )
            );
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(ClasspathCsvConnection.URL_PREFIX) || url.startsWith(FileCsvConnection.URL_PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean jdbcCompliant() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }
}
