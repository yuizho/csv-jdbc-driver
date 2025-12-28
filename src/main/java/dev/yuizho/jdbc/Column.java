package dev.yuizho.jdbc;

public record Column(
        String tableName,
        String name,
        int dataType,
        String typeName
) {
}
