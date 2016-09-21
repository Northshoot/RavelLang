package ai.harmony.ravel.translators;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

/**
 * Created by lauril on 8/23/16.
 */
public class xLingual implements Translator {
    String mName;

    public xLingual(String name){
        this.mName = name;
    }
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

    public String getNameCamelCaseLowerCase() {
        return WordUtils.uncapitalize(getNameCamelCase());
    }
}
