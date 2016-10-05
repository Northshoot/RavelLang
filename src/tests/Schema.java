package tests;

/**
 * Created by lauril on 10/5/16.
 */
public class Schema {
    public String type;
    public String name;
    public String comment;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public Schema(String t, String n, String c) {
        type = t;
        name = n;
        comment = c;
    }
}

