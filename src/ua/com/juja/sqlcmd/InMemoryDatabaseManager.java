package ua.com.juja.sqlcmd;

import java.util.Arrays;

public class InMemoryDatabaseManager implements DatabaseManager {

    public static final String TABLE_NAME = "users";
    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public String[] getTableNames() {
        String []result = new String[]{ TABLE_NAME };
        return result;
    }

    @Override
    public DataSet[] getTableData(String tableName) {
        return Arrays.copyOf(data, freeIndex);
    }

    @Override
    public void connect(String databaseName, String login, String password) {
        //do nothing
    }

    @Override
    public void clear(String tableName) {
        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public void create(DataSet input) {
        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        for (int index = 0; index < freeIndex; index++) {
            if(data[index].get("id") == (Object)id){
                data[index].updateFrom(newValue);
            }
        }
    }
}
