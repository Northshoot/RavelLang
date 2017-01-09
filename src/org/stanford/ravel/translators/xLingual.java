package org.stanford.ravel.translators;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

/**
 * Created by lauril on 8/23/16.
 */
public class xLingual implements Translator {
    public String mName;
    public String mGivenName;
    //TODO: fix naming for files
    public String getFileNameC(){
        return mName.toLowerCase();
    }

    public xLingual(String givenName, String internalName){
        this.mGivenName = givenName;
        this.mName = internalName;
    }

    public xLingual(String givenName){
        this.mGivenName = givenName;
        this.mName = givenName;
    }

    public String getHeaderFileName(){
        return getCName() + ".h";
    }
    public String getObjFileName(){
        return getCName() + ".c";
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
    public String getCName(){
        return StringUtils.join(mName.split("(?=\\p{Upper})"),"_").toLowerCase();
    }

    @Override
    public String getCVarName() {
        return "m_" + StringUtils.join(mName.split("(?=\\p{Upper})"), "_").toLowerCase();
    }

    @Override
    public String getDefineName(){
        return getCName().toUpperCase();
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
        return mGivenName;
    }

    public String getNameUpper() {
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
