package ai.harmony.ravel.compiler.symbol;

/**
 * Created by lauril on 4/24/17.
 */
public class ImportSymbol extends BaseSymbol {

    public String from = null;
    public String as = null;
    public String name = null;

    public ImportSymbol(String name) {
        super(name);
    }


    public void setFrom(String from){
        this.from = from;
    }

    /**
     * Set dotted name that has to be transformed to path
     * @param name dotted import name
     * TODO: handle multiple platforms
     */
    public void setName(String name){
        this.name = name;
    }

    //TODO: not supported now
    public void setAs(String as){
        this.as = as;
    }
    /**
     * Construct path from data
     * @return
     */
    public String getPath(){
        //Do the actual conversion
        String path_from="";
        String path_name="";
        String ret = "";
        if (this.from != null){
            path_from = from.replace(".","/") +"/";
        }
        if (this.name != null){
            path_name = name.replace(".","/");
        }

        return path_from  + path_name +".rv";
    }

    @Override
    public String toString(){
        return getPath();
    }
}
