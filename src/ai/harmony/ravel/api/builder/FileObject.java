package ai.harmony.ravel.api.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by lauril on 10/3/16.
 */
public class FileObject {

    // base path to which all files belong
    private String basePath;
    // sub folder containing this file (eg java package folder, or api/ in C)
    private String subPath = "";
    //name of the file
    private String fileName;
    //data to be writen
    private String content;

    public String getPath() {
        if (basePath == null) throw new IllegalStateException("File Path is not set! " + fileName);
        return basePath + subPath;
    }
    public String getRelativeName() {
        if (subPath == null || subPath.isEmpty())
            return fileName;
        else
            return subPath + "/" + fileName;
    }

    public String getBasePath() {
        return basePath;
    }
    public String getSubPath() {
        return subPath;
    }
    public void setBasePath(String path) {
        this.basePath = path;
        if (!this.basePath.endsWith("/"))
            this.basePath = this.basePath + "/";
    }
    public void setSubPath(String path) {
        this.subPath = path;
    }

    public void addSubPath(String path) {
        if (subPath == null)
            subPath = path;
        else
            subPath = path + '/' + subPath;
    }

    public String getFileName() {
        assert fileName != null;
        return fileName;
    }
    public void setFileName(String name) {
        this.fileName = name;
    }

    private String getContent() {
        return content;
    }

    public void setContent(String data) {
        this.content = data;
    }

    public void appendContent(String data) {
        if(this.content == null) {
            this.content = data;
        } else {
            this.content+="\n";
            this.content+=data;
        }
    }


    public void toFile() {
        try {
            File f = new File(getPath(), getFileName());
            File dir = f.getParentFile();
            if (!dir.exists()) {
                if (!dir.mkdirs())
                    throw new IOException("Failed to create output directory");
            }
            FileWriter w = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(getContent());
            bw.close();
            w.close();
        }
        catch (IOException ioe) {
            System.err.println("can't write file");
            ioe.printStackTrace(System.err);
        }
    }

    public String toString(){
        String ret = "File object " + fileName +"\n";
        ret+="Destination " + getPath() + "\n";
        ret+=content;
        return ret;
    }
    public FileObject(){

    }


}
