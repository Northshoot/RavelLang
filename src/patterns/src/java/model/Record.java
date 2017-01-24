package patterns.src.java.model;

import java.io.Serializable;

/**
 * Created by lauril on 1/23/17.
 */
public class Record implements Serializable{
    int possition;
    int state;
    int field1;
    int field2;
    int field3;
    int field4;

    public Record(int possition,
                  int field1_val,
                  int field2_val,
                  int field3_val,
                  int field4_val){

        this.possition = possition;
        this.field1 = field1_val;
        this.field2 = field2_val;
        this.field3 = field3_val;
        this.field4 = field4_val;
    }

    public Record(){}
}
