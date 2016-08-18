package ai.harmony.ravel.primitives;

import ai.harmony.ravel.antlr4.RavelParser;
import org.apache.commons.lang3.text.WordUtils;
import java.util.Locale;


/**
 * Created by lauril on 8/8/16.
 */
public class Field {
    private final String mName;
    //will come handy in use to add documentation when generating
    //eahc field
//    private final String mDocumentation;
    private FieldType mFieldType;
    private boolean mIsAutoIncrement;
    private String mDefaultValue;
    private Model mModel;

    public Field(Model model,
                 String name,
                // String documentation,
                 FieldType type,
                 boolean isAutoIncrement,
                 String defaultValue){
        mModel = model;
        mName = name;
//        mDocumentation = documentation;
        mFieldType = type;

        mIsAutoIncrement = isAutoIncrement;
        mDefaultValue = defaultValue;

    }

    public Field(Model model, RavelParser.FieldDeclarationContext ctx){
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



    public FieldType getType() {
        return mFieldType;
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
