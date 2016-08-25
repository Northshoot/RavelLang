package ai.harmony.ravel.primitives;

import ai.harmony.ravel.compiler.old.Symbol;
import static ai.harmony.ravel.primitives.Field.Type.*;



/**
 * Created by lauril on 8/8/16.
 */
public class Field extends Primitive{
    private Field.Type mFieldType;
    private String mDefaultValue;
    private Model mModel;
    private String mTypeName;

    public enum Type {  T_INTEGER, T_NUMBER, T_DATE,
                        T_DATE_TIME, T_TIME_STAMP, T_BYTE,
                        T_STRING, T_BOOLEAN, T_CONTEXT, tINVALID
                    }

    public Field(String name,
                 String mTypeName,
                 Model model,
                 Field.Type type){
        super(name);
        this.mModel = model;
        this.mTypeName = mTypeName;
        this.mName = name;
        this.mFieldType = type;
        this.mDefaultValue = "";

    }




    public Field.Type getType() {
        return mFieldType;
    }



    public String getDefaultValue() {
        return mDefaultValue;

    }

    public boolean getHasDefaultValue() {
        return mDefaultValue != null && mDefaultValue.length() > 0;
    }

    public int getByteSize() {
        switch (mFieldType){
            case T_BOOLEAN :   return 2;
            case T_BYTE :   return 10;
            case T_DATE_TIME :  return 4;
            case T_DATE :   return 4;
            case T_INTEGER :   return 4;
            case T_NUMBER:   return 4;
            case T_TIME_STAMP :   return 4;
            case T_STRING :   return 4;
            case T_CONTEXT:   return 4;

            case tINVALID:
                return 0;
        }
        return 0;
    }

    public String getVerboseName(){
        return super.getVerboseName();
    }


    public String getJavaType(){
        switch (mFieldType) {
            case T_BOOLEAN:
                return "boolean";
            case T_BYTE:
                return "byte[]";
            case T_DATE:
                return "Date";
            case T_DATE_TIME:
                return "LocalDateTime";
            case T_INTEGER:
                return "int";
            case T_NUMBER:
                return "float";
            case T_TIME_STAMP:
                return "long";
            case T_STRING:
                return "String";
            default:
                //TODO: through an error
                return null;
        }
    }

    public String getCType(){
        switch (mFieldType) {
            case T_BOOLEAN:
                return "bool";
            case T_BYTE:
                return "char[]";
            case T_DATE:
                return "uint32_t";
            case T_DATE_TIME:
                return "uint32_t";
            case T_INTEGER:
                return "uint32_t";
            case T_NUMBER:
                return "float";
            case T_TIME_STAMP:
                return "uint32_t";
            case T_STRING:
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
        switch (mFieldType) {
            case T_BOOLEAN:
                return "BooleanField";
            case T_BYTE:
                return "ByteField";
            case T_DATE:
                return "DateField";
            case T_DATE_TIME:
                return "DateTimeField";
            case T_INTEGER:
                return "IntegerField";
            case T_NUMBER:
                return "NumberField";
            case T_TIME_STAMP:
                return "TimeStampField";
            case T_STRING:
                return "StringField";
            default:
                //TODO: through an error
                return null;
        }
    }

    @Override
    public String toString(){
        return "Field[name: " + getName() + ", type: " +
                mTypeName + ", model: " + mModel.getName()+
                "]";
    }
    public static Field.Type getType(Symbol.Type tokenType) {
        switch (tokenType){
            case T_BOOLEAN_FIELD :   return Type.T_BOOLEAN;
            case T_BYTE_FIELD :   return T_BYTE;
            case T_DATE_FIELD :  return T_DATE;
            case T_DATE_TIME_FIELD :   return T_DATE_TIME;
            case T_INTEGER_FIELD :   return T_INTEGER;
            case T_NUMBER_FIELD :   return T_NUMBER;
            case T_TIME_STAMP_FIELD :   return T_TIME_STAMP;
            case T_STRING_FIELD :   return T_STRING;
            case T_CONTEXT_FIELD :   return T_CONTEXT;

            case tINVALID:
                break;
        }
        return tINVALID;
    }




}
