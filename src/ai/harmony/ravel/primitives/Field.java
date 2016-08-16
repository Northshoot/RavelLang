package ai.harmony.ravel.primitives;

import org.apache.commons.lang3.text.WordUtils;
import java.util.Locale;


/**
 * Created by lauril on 8/8/16.
 */
public class Field {
    private final String mName;
    private final String mDocumentation;
    private final Type mType;
    private boolean mIsId;
    private final boolean mIsIndex;
    private final boolean mIsNullable;
    private final boolean mIsAutoIncrement;
    private final String mDefaultValue;
    private final Model mModel;

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
        mDocumentation = documentation;
        mType = type
        mIsId = isId;
        mIsIndex = isIndex;
        mIsNullable = isNullable;
        mIsAutoIncrement = isAutoIncrement;
        mDefaultValue = defaultValue;

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


    public String getDocumentation() {
        return mDocumentation;
    }


}
