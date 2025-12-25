package dev.yuizho.jdbc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class CsvDriver implements Driver {
    private static final String EXPECTED_URL_PREFIX = "jdbc://";

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!url.startsWith(EXPECTED_URL_PREFIX)) {
            throw new IllegalArgumentException("urlはjdbc:classpath://形式で入力してください。");
        }

        return new CsvConnection(url.replace(EXPECTED_URL_PREFIX, ""));
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        throw new UnsupportedOperationException();
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
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }
}
