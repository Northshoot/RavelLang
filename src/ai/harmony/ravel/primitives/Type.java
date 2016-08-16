package ai.harmony.ravel.primitives;

/**
 * Created by lauril on 8/15/16.
 */
public class Type {
    private static final int TYPE_STRING = 0; //"String";
    private static final int TYPE_INTEGER = 1; //"Integer";
    private static final int TYPE_LONG = 2; //"Long";
    private static final int TYPE_FLOAT = 3; // "Float";
    private static final int TYPE_DOUBLE = 4; //"Double";
    private static final int TYPE_BOOLEAN = 5; //"Boolean";
    private static final int TYPE_DATE = 6; //"Date";
    private static final int TYPE_BYTE_ARRAY = 7;//"byte[]";

    private final int mType;

    public Type(int type){
        mType = type;
    }

    public String getJavaType(){
        switch (mType){
            case Type.TYPE_BOOLEAN:
                return "boolean";
            case Type.TYPE_BYTE_ARRAY:
                return "byte[]";
            case Type.TYPE_DATE:
                return "Date";
            case Type.TYPE_DOUBLE:
                return "";
            case Type.TYPE_FLOAT:
                return "float";
            case Type.TYPE_INTEGER:
                return "int";
            case Type.TYPE_LONG:
                return "long";
            case Type.TYPE_STRING:
                return "String";

        }
        return null;
    }

    public String getCType(){
        switch (mType){
            case Type.TYPE_BOOLEAN:
                return "bool";
            case Type.TYPE_BYTE_ARRAY:
                return "char[]";
            case Type.TYPE_DATE:
                return "int"; //milliseconds
            case Type.TYPE_DOUBLE:
                return "double";
            case Type.TYPE_FLOAT:
                return "float";
            case Type.TYPE_INTEGER:
                return "int32_t";
            case Type.TYPE_LONG:
                return "int64_t";
            case Type.TYPE_STRING:
                return "char[]";

        }
        return null;
    }

    public String getPythonType(){
        return "";
    }

    public String getDjangoType(){
        switch (mType){
            case Type.TYPE_BOOLEAN:
                return "fields.NullBooleanField()";
            case Type.TYPE_BYTE_ARRAY:
                return "fields.BinaryField()";
            case Type.TYPE_DATE:
                return "fields.DateTimeField()";
            case Type.TYPE_DOUBLE:
                return "fields.DecimalField()";
            case Type.TYPE_FLOAT:
                return "fields.FloatField()";
            case Type.TYPE_INTEGER:
                return "fields.IntegerField()";
            case Type.TYPE_LONG:
                return "fields.BigIntegerField()";
            case Type.TYPE_STRING:
                return "fields.textField()";

        }
        return null;
    }
}
