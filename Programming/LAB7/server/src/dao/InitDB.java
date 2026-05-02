package dao;

import java.nio.file.Files;
import java.nio.file.Path;

public class InitDB {
    public void run(String fileName, DBManager db) throws Exception {
        String sql = Files.readString(Path.of(fileName));
        db.ddlUpdate(sql);
    }
}