package kr.co.hs.database;


/**
 * Created by Bae on 2016-11-25.
 */
public class HsSqlCreate extends HsSql {
    private static final String FORMAT = "CREATE TABLE %s(%s)";

    private String sql;

    public HsSqlCreate(String tableName, Column... columns){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<columns.length;i++){
            if(i == columns.length-1){
                buffer.append(columns[i].getColumn());
            }else{
                buffer.append(columns[i].getColumn());
                buffer.append(", ");
            }
        }
        sql = String.format(FORMAT, tableName, buffer.toString());
    }

    @Override
    public String toString() {
        return sql;
    }

    public static class Column implements HsSqlConstant{
        private static final String BASE_FORMAT = "%s %s";
        private static final String DEFAULT_VALUE_FORMAT = "%s %s DEFAULT %s";
        private static final String PRIMARYKEY_VALUE_FORMAT = "%s PRIMARY KEY";
        private static final String AUTOINCREMENT_KEY_FORMAT = "%s PRIMARY KEY AUTOINCREMENT";

        private String mColumn;
        private String mMode = null;

        public Column(String type, String name){
            mColumn = String.format(BASE_FORMAT, name, type);
        }

        public Column(String type, String name, String defaultVal){
            mColumn = String.format(DEFAULT_VALUE_FORMAT, name, type, defaultVal);
        }

        public Column setPrimaryKey(){
            mMode = PRIMARYKEY_VALUE_FORMAT;
            return this;
        }

        public Column setAutoIncrement(){
            mMode = AUTOINCREMENT_KEY_FORMAT;
            return this;
        }

        public Column setDefault(){
            mMode = null;
            return this;
        }

        private String getColumn(){
            if(mMode == null)
                return mColumn;
            else{
                return String.format(mMode, mColumn);
            }
        }
    }
}
