package dao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class DAO {
    // Пулл соединений
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("postgres");

        config.setMaximumPoolSize(5); // Максимальное количество соединений в пуле
        config.setMinimumIdle(2);      // Минимальное количество простаивающих соединений
        config.setConnectionTimeout(5000); // Время ожидания свободного соединения
        config.setIdleTimeout(600000); // Время жизни простаивающего соединения

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }
    }

    public ResultSet select(String sql, Object... params) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
                // Копируем в него данные из ResultSet
                crs.populate(rs);
                return crs;
            }
        }
    }
}
