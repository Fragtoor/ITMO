package dao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class DAO {
    // Пулл соединений
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("properties/application.properties")) {
            props.load(in);
            String hostDB = props.getProperty("server.db.host");
            int portDB = Integer.parseInt(props.getProperty("server.db.port"));
            String nameDB = props.getProperty("server.db.name");
            String user = props.getProperty("server.db.user");
            String password = props.getProperty("server.db.password");
            config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s", hostDB, portDB, nameDB));
            config.setUsername(user);
            config.setPassword(password);

            config.setMaximumPoolSize(20); // Максимальное количество соединений в пуле
            config.setMinimumIdle(2);      // Минимальное количество простаивающих соединений
            config.setConnectionTimeout(5000); // Время ожидания свободного соединения
            config.setIdleTimeout(600000); // Время жизни простаивающего соединения
            dataSource = new HikariDataSource(config);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
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

    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
                crs.populate(rs);
                return crs;
            }
        }
    }
}
