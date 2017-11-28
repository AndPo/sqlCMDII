package ua.com.juja.sqlcmd;

public interface DatabaseManager {

    String[] getTableNames();

    DataSet[] getTableData(String tableName);

    void connect(String databaseName, String login, String password);

    void clear(String tableName);

    void create(DataSet input);

    void update(String tableName, int id, DataSet newValue);


}
