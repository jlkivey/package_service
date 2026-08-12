package com.clevelanddx.packageintake.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * One-off runner: when -Ddb.get.definition=schemaname.objectname is set,
 * queries the database for that object's definition (view/table) and prints it, then exits.
 */
@Component
@Order(Integer.MAX_VALUE)
public class DefinitionQueryRunner implements CommandLineRunner {

    private static final String PROP = "db.get.definition";

    private final DataSource dataSource;

    public DefinitionQueryRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        String objectName = System.getProperty(PROP);
        if (objectName == null || objectName.isBlank()) {
            return;
        }

        String object = objectName.trim();
        String[] parts = object.split("\\.", 2);
        String schemaName = parts.length == 2 ? parts[0] : "dbo";
        String objectNameOnly = parts.length == 2 ? parts[1] : parts[0];

        try (Connection conn = dataSource.getConnection()) {
            // Try view/proc/function definition first
            String sql = "SELECT OBJECT_DEFINITION(OBJECT_ID(?)) AS definition";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, object);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String definition = rs.getString("definition");
                        if (definition != null) {
                            System.out.println("-- Definition of " + object);
                            System.out.println(definition);
                            System.exit(0);
                            return;
                        }
                    }
                }
            }

            // If no definition, try table structure (reports.visits2 may be a table)
            String tableSql = "SELECT c.name AS column_name, t.name AS type_name, c.max_length, c.precision, c.scale, c.is_nullable "
                + "FROM sys.columns c "
                + "JOIN sys.types t ON c.user_type_id = t.user_type_id "
                + "WHERE c.object_id = OBJECT_ID(?) ORDER BY c.column_id";
            try (PreparedStatement ps = conn.prepareStatement(tableSql)) {
                ps.setString(1, object);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("-- " + object + " (table structure)");
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(rs.getString("column_name")).append(" ")
                                .append(rs.getString("type_name"));
                            int maxLen = rs.getInt("max_length");
                            if (maxLen > 0 && rs.getString("type_name").toLowerCase().contains("char")) {
                                sb.append("(").append(maxLen == -1 ? "max" : maxLen).append(")");
                            }
                            sb.append(rs.getInt("is_nullable") == 1 ? " NULL" : " NOT NULL").append("\n");
                        } while (rs.next());
                        System.out.println(sb);
                    } else {
                        System.out.println("Object not found: " + object);
                    }
                }
            }
        }
        System.exit(0);
    }
}
