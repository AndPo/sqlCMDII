package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Arrays;

public class DatabaseManager {

    private Connection connection;

    public static void main(String[] args) throws  ClassNotFoundException, SQLException{

        String database = "postgres";
        String login = "postgres";
        String password = "postgres";

        DatabaseManager manager = new DatabaseManager();

        manager.connect(database, login, password);

        Connection connection = manager.getConnection();

        //clear table
        manager.clear("users");

        //insert
        DataSet data = new DataSet();
        data.put("name", "Andriy");
        data.put("surname", "Popovych");
        data.put("id", 9);
        manager.create(data);

        //select
//        String [] tableNames = manager.getTableNames();
//        tableNames.toString();


        //get data
//        DataSet[] tablesData = manager.getTableData("users");
//        for (int i = 0; i < tablesData.length; i++) {
//            System.out.println(tablesData[i].toString());
//        }





        System.out.println("it's working!!!");

    }

    public void create(DataSet input) {
        try {


            String tableNames = "";
            for (String name: input.getNames()) {
                tableNames += name + ",";
            }
            tableNames = tableNames.substring(0, tableNames.length() - 1);

            String values = "";
            for (Object value: input.getValues()){
                values += "'" + value.toString() + "'" + ",";
            }
            values = values.substring(0, values.length()-1);

            Statement stmt = connection.createStatement();
              //                              (" + tableNames +  ") "    <-----------------
            stmt.executeUpdate("INSERT INTO users " +
                    "VALUES " + values + ");");

          //todo  insert into users(name, surname, id) values('Pops', 'Perops', 13);

            stmt.close();
        } catch (SQLException e) {
            System.out.println("create method didn't work " );
            e.printStackTrace();
        }
    }



    public String[] getTableNames(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM information_schema.tables" +
                    " WHERE table_schema = 'public' AND table_type = 'BASE TABLE'");
            int index = 0;
            String[] tables = new String[100];
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            rs.close();
            stmt.close();
            return tables;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public DataSet[] getTableData(String tableName) {
        try {
            int size = getSize(tableName);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnSize = rsmd.getColumnCount();
            DataSet[]result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for(int i = 1; i <= columnSize; i++) {
                  dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        }catch(SQLException ex){
            System.out.println("TableData problem...");
            ex.printStackTrace();
            return null;
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT (*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    private Connection getConnection() {
        return connection;
    }

    public void connect(String databaseName, String login, String password){
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException ex){
            System.out.println("connect your jdbc jar!");
            ex.printStackTrace();
        }try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseName,
                    login, password);

        }catch(SQLException ex){
            System.out.println(String.format("Can`t get connection to databas: %s user:%s", databaseName, login, password));
            connection = null;
        }

    }

    public void clear(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM " +  tableName);
            stmt.close();
            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("problem with clearing table " + tableName);
        }
    }
}
