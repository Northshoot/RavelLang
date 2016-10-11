package ai.harmony.api.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by lauril on 10/3/16.
 */
public class FileObject {

    //path to which data is written
    private String path;
    //name of the file
    private String fileName;
    //data to be writen
    private String content;

    public String getPath() throws IOException{
        if (path == null) throw new IOException("File Path is not set! " + fileName);
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() throws IOException{
        if (fileName == null) {
            throw new IOException("File name is not set!");
        } else {
            return fileName;
        }
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public String getContent() throws IOException{
        if (content == null) {
            throw new IOException("File content is not set! " + fileName);
        } else {
            return content;
        }
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
            if ( !f.getParentFile().exists() ) f.getParentFile().mkdirs();
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
        ret+="Destination " + path + "\n";
        ret+=content;
        return ret;
    }
    public FileObject(){

    }


}
