package dev.yuizho.jdbc;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class FileCsvConnection implements Connection {
    public static final String URL_PREFIX = "jdbc:file://";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCsvConnection.class);
    private final String url;

    public FileCsvConnection(String url) {
        this.url = url.replace(URL_PREFIX, "");
    }

    @Override
    public Statement createStatement() throws SQLException {
        LOGGER.info("FileCsvConnection#createStatement");
        var path = Path.of(this.url);
        try {
            var is = Files.newInputStream(path);
            return new CsvStatement(is);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        LOGGER.info("FileCsvConnection#close");
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        var path = Path.of(this.url);
        var fileName = path.getFileName().toString().replace(".csv", "");
        System.out.println(fileName);
        try (var is = Files.newInputStream(path)) {
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()                // ヘッダーがあることを指定（1行目をヘッダーとして認識）
                    .setSkipHeaderRecord(true)  // ヘッダー行をレコードのループ処理に含めない
                    .build();
            var csv = CSVParser.parse(
                    is,
                    StandardCharsets.UTF_8,
                    format
            );

            return new CsvDatabaseMetaData(
                    new Table(
                            "",
                            "",
                            fileName,
                            "TABLE",
                            ""
                    ),
                    csv.getHeaderNames()
                            .stream()
                            .map(name -> new Column(
                                    fileName,
                                    name,
                                    Types.VARCHAR,
                                    "VARCHAR"
                            ))
                            .toList()
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        LOGGER.info("FileCsvConnection#isValid");
        // TODO: コネクションプール向けにインチキして常にtrue返してる。ちゃんとやるならファイルが読める状態化とかを調べる。

        return true;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        LOGGER.info("FileCsvConnection#isReadOnly");
        // コネクションプール向けにインチキして常にtrue返してる。

        return true;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        LOGGER.info("FileCsvConnection#setReadOnly");
        // コネクションプール向けにインチキしてなにもしない
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        LOGGER.info("FileCsvConnection#getAutoCommit");
        return true;
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        LOGGER.info("FileCsvConnection#getTransactionIsolation");
        return Connection.TRANSACTION_NONE;
    }

    @Override
    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void commit() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCatalog() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
