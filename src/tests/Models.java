package tests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lauril on 10/5/16.
 */
public class Models {

    public String mName;
    public List<Schema> schema = new ArrayList<>();

    public Models(String name) {
        mName = name;
        for (int i = 0; i < 3; i++) {
            schema.add(new Schema("uint32_t", "m__" + mName + "__" + String.valueOf(Math.random()), "this is a comment"));

            System.out.println(schema.size());
        }
    }

    public String getName() {
        return mName;
    }
    public boolean isReplicated(){ return false;}
    public boolean isLocal(){ return true;}
    public boolean isStreaming(){ return true;}
    public List<Schema> getSchema() {
        System.out.println(schema.get(0).getName());
        return  schema;}
}
