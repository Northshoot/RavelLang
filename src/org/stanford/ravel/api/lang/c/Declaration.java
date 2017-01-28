package org.stanford.ravel.api.lang.c;

/**
 * Created by lauril on 9/21/16.
 */
public class Declaration {
    public String mComment = "";
    public String mName = "";

    public Declaration() {
    }

    public Declaration(String mName) {
        this.mName = mName;
        this.mComment = "//** Autogenrated comment for declaration type with name " + this.mName + " */";
    }

    public Declaration(String mName, String mComment) {
        this.mComment = "//** " + mComment + " */";
        this.mName = mName;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = "//** " + mComment + " */";
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
