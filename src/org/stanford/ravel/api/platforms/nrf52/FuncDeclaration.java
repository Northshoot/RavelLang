package org.stanford.ravel.api.platforms.nrf52;

// FIXME: eliminate this code when NRF52 has been ported away

import org.stanford.ravel.api.platforms.nrf52.Declaration;

/**
 * Created by lauril on 9/22/16.
 */
public class FuncDeclaration extends Declaration {
    public String mMethodDeclaration;
    public String mCallFunction;
    public String mReturnType = "void";
    public String mFunctionImplementation;

    public FuncDeclaration(String mName, String mMethodDeclaration, String mCallFunction, String mReturnType) {
        super(mName);
        this.mMethodDeclaration = mMethodDeclaration;
        this.mCallFunction = mCallFunction;
        this.mReturnType = mReturnType;
    }

    public String getFunctionImplementation() {
        return mFunctionImplementation;
    }

    public void setFunctionImplementation(String mFunctionImplementation) {
        this.mFunctionImplementation = mFunctionImplementation;
    }

    public FuncDeclaration(String mName, String mComment, String mMethodDeclaration, String mCallFunction, String mReturnValue) {
        super(mName, mComment);
        this.mMethodDeclaration = mMethodDeclaration;
        this.mCallFunction = mCallFunction;
        this.mReturnType = mReturnValue;
    }

    public FuncDeclaration() { super(); }

    @Override
    public void setName(String name){
        super.mName = name;
        setCallFunction( super.mName+"();");
    }

    public String getMethodDeclaration() {
        return mMethodDeclaration;
    }

    public void setMethodDeclaration(String mMethodDeclaration) {
        this.mMethodDeclaration = mMethodDeclaration;
    }

    public String getCallFunction() {
        return mCallFunction;
    }

    public void setCallFunction(String mCallFunction) {
        this.mCallFunction = mCallFunction;
    }

    public String getReturnType() {
        return mReturnType;
    }

    public void setReturnValue(String mReturnValue) {
        this.mReturnType = mReturnValue;
    }
}
