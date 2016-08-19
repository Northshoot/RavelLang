package ai.harmony.ravel.primitives;

import ai.harmony.ravel.compiler.symbols.Symbol;

/**
 * Created by lauril on 8/15/16.
 */
public class FieldType  {

    private Symbol.Type mType;
    private String name;


    public FieldType(Symbol.Type type){
        mType = type;
        switch (mType){
            case T_BOOLEAN_FIELD:
                name =  "BooleanField";
            case T_BYTE_FIELD:
                name =  "ByteField";
            case T_DATE_FIELD:
                name =  "DateField";
            case T_DATE_TIME_FIELD:
                name =  "DateTimeField";
            case T_INTEGER_FIELD:
                name =  "IntegerField";
            case T_NUMBER_FIELD:
                name =  "NumberField";
            case T_TIME_STAMP_FIELD:
                name =  "TimeStampField";
            case T_STRING_FIELD:
                name =  "StringField";
        }

    }

    public String getVerboseName(){
        return name;
    }

    public String getJavaType(){
        switch (mType) {
            case T_BOOLEAN_FIELD:
                return "boolean";
            case T_BYTE_FIELD:
                return "byte[]";
            case T_DATE_FIELD:
                return "Date";
            case T_DATE_TIME_FIELD:
                return "LocalDateTime";
            case T_INTEGER_FIELD:
                return "int";
            case T_NUMBER_FIELD:
                return "float";
            case T_TIME_STAMP_FIELD:
                return "long";
            case T_STRING_FIELD:
                return "String";
            default:
                //TODO: through an error
                return null;
        }
    }

    public String getCType(){
        switch (mType) {
            case T_BOOLEAN_FIELD:
                return "bool";
            case T_BYTE_FIELD:
                return "char[]";
            case T_DATE_FIELD:
                return "uint32_t";
            case T_DATE_TIME_FIELD:
                return "uint32_t";
            case T_INTEGER_FIELD:
                return "uint32_t";
            case T_NUMBER_FIELD:
                return "float";
            case T_TIME_STAMP_FIELD:
                return "uint32_t";
            case T_STRING_FIELD:
                return "char[]";
            default:
                //TODO: through an error
                return null;
        }
    }

    public String getPythonType(){
        return "";
    }

    public String getDjangoType() {
        switch (mType) {
            case T_BOOLEAN_FIELD:
                return "BooleanField";
            case T_BYTE_FIELD:
                return "ByteField";
            case T_DATE_FIELD:
                return "DateField";
            case T_DATE_TIME_FIELD:
                return "DateTimeField";
            case T_INTEGER_FIELD:
                return "IntegerField";
            case T_NUMBER_FIELD:
                return "NumberField";
            case T_TIME_STAMP_FIELD:
                return "TimeStampField";
            case T_STRING_FIELD:
                return "StringField";
            default:
                //TODO: through an error
                return null;
        }
    }
}
