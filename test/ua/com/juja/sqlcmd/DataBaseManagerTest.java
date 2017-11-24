package ua.com.juja.sqlcmd;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.util.Arrays;

public class DataBaseManagerTest {

    private DatabaseManager manager;
    @Before
    public void setup(){
        manager = new DatabaseManager();
        manager.connect("postgres", "postgres", "postgres");

    }

    @Test
    public void testGetAllTableNames(){
        String[] tableNames = manager.getTableNames();
        assertEquals("[users]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTablesData(){
        //given
        manager.clear("users");

        //when
        DataSet input = new DataSet();
        input.put("name", "Andriy");
        input.put("surname", "Popovych");
        input.put("id", "9");
        manager.create(input);

        //then
        DataSet[] users = manager.getTableData("users");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, surname, id]", Arrays.toString(user.getNames()));
        assertEquals("[Andriy, Popovych, 9]", Arrays.toString(user.getValues()));

    }

}
