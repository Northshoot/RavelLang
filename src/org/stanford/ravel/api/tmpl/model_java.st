package ai.harmony.app;

import android.util.Log;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import ai.harmony.ravel.utils.ByteWork;

public class <name> {
    private static final String TAG = "<model_name>";
    private Queue<<record_name>> queue;
    private int size=50;
    private int counter = 0;

    public <model.name>(){
        queue = new LinkedList<>();
    }

    public void add(String time, String random){
        <model.name>Record rr;
        if (is_full()){
            rr = queue.remove();
            Log.d(TAG, "Removed record: " + rr);

        }
        counter++;
        rr = new <name>Record(time, random, counter);
        queue.add( rr );
        Log.d(TAG, "Added record: " + rr);
    }

    public <name>Record getRecord(){
        return queue.poll();
    }

    private boolean is_full(){
        return queue.size() == size;
    }

    public void add(byte[] data) {
        //perform serialization
    }

        public class <model.name>Record {
        <model.schema.fields:{field|<field.type.javaType> <field.javaVarName> };separator="\n">
            int local_num=0;

            public <model.name>Record(<model.schema.fields:{field|<field.type.javaType> <field.javaName> }separator=",">){
                <model.schema.fields:{field|this.<field.javaVarName> = <field.javaName>};separator="\n">
            }

            @Override
            public String toString() {
                return "Record #: " + local_num +
                <model.schema.fields:{field|" <field.verboseName> " + <field.javaName> }separator="+">;
            }
        }
}