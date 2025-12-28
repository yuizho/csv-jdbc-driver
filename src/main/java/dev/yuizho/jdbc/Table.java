package dev.yuizho.jdbc;

public record Table(
        String cat,
        String schem,
        String name,
        String type,
        String remarks
) {
}
