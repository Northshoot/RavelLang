package ai.harmony.ravel.primitives.Fields;

import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.primitives.Model;
import ai.harmony.ravel.translators.Translator;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static ai.harmony.ravel.primitives.Fields.Field.Type.*;



/**
 * This is a base class for model fields
 * that has to be subclasses by concreate fields
 * the solution is adapted from
 * java the complete reference, ninth edition generics
 * with ideas from
 * http://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class
 * and
 * http://egalluzzo.blogspot.com/2010/06/using-inheritance-with-fluent.html
 * the later has several error in it but gives good iea on what NOT to do
 * Created by lauril on 8/8/16.
 */
public abstract class Field<T> implements Translator{
    // field have to must things
    //name and
    protected String mName;
    protected Field.Type mFieldType;
    protected String mTypeName;
    //each field belongs to a model
    protected Model mModel;
    //we always save a copy of default value as a string
    protected boolean mHasOptions = false;
    protected String mDefaultValueString;
    protected T mDefaultValue;
    protected String mDocumentation ="Auto Generated";
    protected Map<String, String> mFieldOptions = new HashMap<>();


    public enum Type {  T_INTEGER, T_NUMBER, T_DATE,
                        T_DATE_TIME, T_TIME_STAMP, T_BYTE,
                        T_STRING, T_BOOLEAN, T_CONTEXT, T_MODEL, tINVALID
                    }

    protected Field() {}

    public static abstract class Builder<T extends Field, B extends Builder<T, B>> {
        protected T obj;
        protected B thisObj;

        protected abstract T createObject();
        protected abstract B thisObject();

        public Builder() {
            obj = createObject();
            thisObj = thisObject();
        }
        public T build() {
            return obj;
        }

        //builder methods for setting property
        public B model(Model m){obj.mModel = m; return thisObj; }
        public B name(String name){obj.mName=name; return thisObj; }
        public B fieldType(Field.Type ft){obj.mFieldType = ft; return thisObj; }
        public B defaultValueString(String d){obj.mDefaultValueString = d; return thisObj; }
        public B defaultValue(T value){obj.mDefaultValue = value; return thisObj; }
        public B fieldTypeName(String ftn){obj.mTypeName = ftn; return thisObj; }
        public B hasOptions(boolean opt){obj.mHasOptions = opt; return thisObj; }
        public B addOption(String name, String value){obj.mFieldOptions.put(name, value); return thisObj;}

    }


    public Field.Type getType() {
        return mFieldType;
    }


    public abstract T getDefaultValue();

    public String getDefaultValueString() {
        return mDefaultValueString;

    }

    public boolean getHasDefaultValue() {
        return mDefaultValueString != null && mDefaultValueString.length() > 0;
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

    //This a translation specific names
    @Override
    public String getVerboseName() {
        return getNameCamelCase();
    }

    @Override
    public String getCStructName() {
        return getNameCamelCase();
    }

    @Override
    public String getCVarName(){
        return "m_"+getNameLowerCase();
    }

    @Override
    public String getJavaClassName() {
        return getName();
    }

    public String getJavaVarName(){
        return "m"+getNameCamelCase();
    }

    @Override
    public String getPythonClassName() {
        return getName();
    }

    @Override
    public String getPythonVarName() {
        return getNameCamelCaseLowerCase();
    }


    public String getName() {
        return mName;
    }

    public String getNameUpperCase() {
        return mName.toUpperCase(Locale.US);
    }

    public String getNameLowerCase() {
        return mName.toLowerCase(Locale.US);
    }

    public String getNameCamelCase() {
        return WordUtils.capitalizeFully(mName, new char[] { '_' }).replaceAll("_", "");
    }

    public  String getCdocumentation(){
        return "// " + getDocumentation();
    }

    public String getJavaDocumentation(){
        return getCdocumentation();
    }
    public  String getPythondocumentation(){
        return "# " + getDocumentation();
    }

    private String getDocumentation(){
        //TODO: the string has to be tokenized to fit 80 chars per line
        if(mHasOptions && mFieldOptions.containsKey("documentations")){
            return mFieldOptions.get("documentations");
        } else {
            return "AUTOGEN-DOCS: " + this.toString();
        }
    }

    public String getNameCamelCaseLowerCase() {
        return WordUtils.uncapitalize(getNameCamelCase());
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
    public static Field.Type getType(int tokenType) {
        switch (tokenType){
            case RavelParser.T_BOOLEAN_FIELD :   return T_BOOLEAN;
            case RavelParser.T_BYTE_FIELD :   return T_BYTE;
            case RavelParser.T_DATE_FIELD :  return T_DATE;
            case RavelParser.T_DATE_TIME_FIELD :   return T_DATE_TIME;
            case RavelParser.T_INTEGER_FIELD :   return T_INTEGER;
            case RavelParser.T_NUMBER_FIELD :   return T_NUMBER;
            case RavelParser.T_TIME_STAMP_FIELD :   return T_TIME_STAMP;
            case RavelParser.T_STRING_FIELD :   return T_STRING;
            case RavelParser.T_CONTEXT_FIELD :   return T_CONTEXT;
            case RavelParser.T_MODEL_FIELD: return T_MODEL;
            default: return tINVALID;
        }

    }




}
