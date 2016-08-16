package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;
import org.apache.commons.lang3.text.WordUtils;
import java.util.Locale;


/**
 * Created by lauril on 8/8/16.
 */
public class Field {
    private final String mName;
    //will come handy in use to add documanetation when generating
    //eahc field
//    private final String mDocumentation;
    private Type mType;
    private boolean mIsAutoIncrement;
    private String mDefaultValue;
    private Model mModel;

    public Field(Model model,
                 String name,
                 String documentation,
                 Type type,
                 boolean isId,
                 boolean isIndex,
                 boolean isNullable,
                 boolean isAutoIncrement,
                 String defaultValue){
        mModel = model;
        mName = name;
//        mDocumentation = documentation;
        mType = type;

        mIsAutoIncrement = isAutoIncrement;
        mDefaultValue = defaultValue;

    }

    public Field(Model model, RavelParser.FieldContext ctx){
        //parse context and create self
        mModel = model;
        mName = ctx.NAME().getText();


    }

    public String getJavaVarName(){
        return "m"+getNameCamelCase();
    }

    public String getVerboseName(){
        return getNameCamelCase();
    }

    public String getJavaName(){
        return getName();
    }

    public String getCName(){
        return getName();
    }

    public String getPythonName(){
        return getName();
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

    public String getNameCamelCaseLowerCase() {
        return WordUtils.uncapitalize(getNameCamelCase());
    }



    public Type getType() {
        return mType;
    }


    public boolean getIsAutoIncrement() {
        return mIsAutoIncrement;
    }

    public String getDefaultValue() {
        return '\'' + mDefaultValue + '\'';

    }

    public boolean getHasDefaultValue() {
        return mDefaultValue != null && mDefaultValue.length() > 0;
    }


//    public String getDocumentation() {
//        return mDocumentation;
//    }


}
