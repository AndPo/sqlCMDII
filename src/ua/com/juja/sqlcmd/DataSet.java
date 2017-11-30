package ua.com.juja.sqlcmd;

import java.util.Arrays;

public class DataSet {

    static class Data{
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100];
    public int freeIndex = 0;


    public void put(String name, Object value) {
        data[freeIndex++] = new Data(name, value);
    }

    public Object[] getValues(){
        Object[] result = new Object[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames(){
        String[] result = new String[freeIndex];
        for (int i = 0; i < freeIndex; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    public Object get(String name) {
        for (int i = 0; i < freeIndex; i++) {
            if(data[i].getName().equals(name)){
                return data[i].getValue();
            }
        }
        return null;
    }

    public void updateFrom(DataSet newValue) {
        for (int i = 0; i < newValue.freeIndex; i++) {
            Data data = newValue.data[i];
            this.put(data.name, data.value);
        }
    }

    @Override
    public String toString(){
        return "DataSet{\n" +
                "names: " + Arrays.toString(getNames()) + "\n" +
                 "values: " + Arrays.toString(getValues()) + "\n" +
                 "}";
    }
}
