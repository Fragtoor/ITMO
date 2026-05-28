package dao;

import common.net.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PermissionsDAO {
    private final DAO dao;

    public PermissionsDAO(DAO dao) {
        this.dao = dao;
    }

    public Set<String> getUserPermissions(User user) throws SQLException {
        Set<String> permissions = new HashSet<>();
        String sql = """
        SELECT p.permissionName
        FROM Permissions p
        JOIN Role_Permissions rp ON p.permissionID = rp.permissionID
        JOIN Users u ON u.roleid = rp.roleID
        WHERE u.login = ?;
        """;

        ResultSet result = dao.executeQuery(sql, user.getLogin());
        while (result.next()) {
            permissions.add(result.getString("permissionName"));
        }
        return permissions;
    }

    public Map<String, List<String>> getAllRolesWithPermissions() throws SQLException {
        Map<String, List<String>> rolesMap = new HashMap<>();
        String sql = """
            SELECT r.roleName, p.permissionName
            FROM Roles r
            LEFT JOIN Role_Permissions rp ON r.roleID = rp.roleID
            LEFT JOIN Permissions p ON rp.permissionID = p.permissionID;
        """;
        ResultSet rs = dao.executeQuery(sql);
        while(rs.next()) {
            String role = rs.getString("roleName");
            String perm = rs.getString("permissionName");
            rolesMap.putIfAbsent(role, new ArrayList<>());
            if (perm != null) rolesMap.get(role).add(perm);
        }
        return rolesMap;
    }

    public List<String> getAllPermissions() throws SQLException {
        List<String> perms = new ArrayList<>();
        ResultSet rs = dao.executeQuery("SELECT permissionName FROM Permissions;");
        while(rs.next()) perms.add(rs.getString("permissionName"));
        return perms;
    }

    public void createPermission(String name) throws SQLException {
        dao.executeUpdate("INSERT INTO Permissions (permissionName) VALUES (?);", name);
    }

    public HashMap<String, String[]> getUsersAndPermissions() throws SQLException {
        HashMap<String, String[]> permissions = new HashMap<>();

        String sql = """
        SELECT u.userid, u.login, r.roleName
        FROM Users u
        JOIN Roles r ON u.roleid = r.roleid;
        """;

        ResultSet result = dao.executeQuery(sql);

        while (result.next()) {
            String userId = String.valueOf(result.getInt("userid"));
            String login = result.getString("login");
            String roleName = result.getString("roleName");
            String[] data = new String[] {login, roleName};
            permissions.put(userId, data);
        }

        return permissions;
    }

    public Map<String, String> getAllPermissionsWithDescription() throws SQLException {
        Map<String, String> perms = new HashMap<>();
        ResultSet rs = dao.executeQuery("SELECT permissionName, description FROM Permissions");

        while(rs.next()) {
            String name = rs.getString("permissionName");
            String desc = rs.getString("description");
            perms.put(name, desc == null ? "Описание отсутствует" : desc);
        }
        return perms;
    }

    public void deletePermission(String permissionName) throws SQLException {
        String upperPerm = permissionName.toUpperCase();

        List<String> protectedPerms = Arrays.asList(
                "READ_COLLECTION", "READ_INFO", "READ_STATS", "SEARCH",
                "CREATE_OBJECT", "UPDATE_OWN", "UPDATE_ALL", "DELETE_OWN",
                "DELETE_ALL", "CLEAR_OWN", "CLEAR_ALL", "VIEW_HISTORY", "ADMIN", "PERMISSION_MANAGE", "ROLE_EDIT"
        );

        if (protectedPerms.contains(upperPerm)) {
            throw new SQLException("Попытка удалить базовую системную функциональность!");
        }

        String sql = "DELETE FROM Permissions WHERE permissionName = ?";
        dao.executeUpdate(sql, upperPerm);
    }
}
