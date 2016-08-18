package ai.harmony.ravel.primitives;

import ai.harmony.ravel.compiler.symbols.Symbol;

/**
 * Created by lauril on 8/15/16.
 */
public class FieldType extends Symbol implements Type {
    public static final int TYPE_STRING = 0; //"String";
    public static final int TYPE_INTEGER = 1; //"Integer";
    public static final int TYPE_LONG = 2; //"Long";
    public static final int TYPE_FLOAT = 3; // "Float";
    public static final int TYPE_DOUBLE = 4; //"Double";
    public static final int TYPE_BOOLEAN = 5; //"Boolean";
    public static final int TYPE_DATE = 6; //"Date";
    public static final int TYPE_BYTE_ARRAY = 7;//"byte[]";
    public static final int TYPE_DATE_TIME = 8; //DateTime

    private int mType;
    private String name;


    public FieldType(int type){
        super("");
        mType = type;
        switch (mType){
            case FieldType.TYPE_BOOLEAN:
                name =  "boolean";
            case FieldType.TYPE_BYTE_ARRAY:
                name =  "byte[]";
            case FieldType.TYPE_DATE:
                name =  "Date";
            case FieldType.TYPE_DOUBLE:
                name =  "Double";
            case FieldType.TYPE_FLOAT:
                name =  "Float";
            case FieldType.TYPE_INTEGER:
                name =  "integer";
            case FieldType.TYPE_LONG:
                name =  "long";
            case FieldType.TYPE_STRING:
                name =  "String";
            case TYPE_DATE_TIME:
                name = "DateTime";

        }
        super.name = name;
    }



    public String getVerboseName(){

        return name;
    }
    public String getJavaType(){
        switch (mType){
            case FieldType.TYPE_BOOLEAN:
                return "boolean";
            case FieldType.TYPE_BYTE_ARRAY:
                return "byte[]";
            case FieldType.TYPE_DATE:
                return "Date";
            case FieldType.TYPE_DOUBLE:
                return "";
            case FieldType.TYPE_FLOAT:
                return "float";
            case FieldType.TYPE_INTEGER:
                return "int";
            case FieldType.TYPE_LONG:
                return "long";
            case FieldType.TYPE_STRING:
                return "String";

        }
        return null;
    }

    public String getCType(){
        switch (mType){
            case FieldType.TYPE_BOOLEAN:
                return "bool";
            case FieldType.TYPE_BYTE_ARRAY:
                return "char[]";
            case FieldType.TYPE_DATE:
                return "int"; //milliseconds
            case FieldType.TYPE_DOUBLE:
                return "double";
            case FieldType.TYPE_FLOAT:
                return "float";
            case FieldType.TYPE_INTEGER:
                return "int32_t";
            case FieldType.TYPE_LONG:
                return "int64_t";
            case FieldType.TYPE_STRING:
                return "char[]";

        }
        return null;
    }

    public String getPythonType(){
        return "";
    }

    public String getDjangoType(){
        switch (mType){
            case FieldType.TYPE_BOOLEAN:
                return "fields.NullBooleanField()";
            case FieldType.TYPE_BYTE_ARRAY:
                return "fields.BinaryField()";
            case FieldType.TYPE_DATE:
                return "fields.DateTimeField()";
            case FieldType.TYPE_DOUBLE:
                return "fields.DecimalField()";
            case FieldType.TYPE_FLOAT:
                return "fields.FloatField()";
            case FieldType.TYPE_INTEGER:
                return "fields.IntegerField()";
            case FieldType.TYPE_LONG:
                return "fields.BigIntegerField()";
            case FieldType.TYPE_STRING:
                return "fields.textField()";

        }
        return null;
    }
}
