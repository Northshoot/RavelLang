package ai.harmony.ravel.primitives;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 7/29/16.
 */
public class Space extends Primitive{
    private String mTransmitFunction;
    private Map<String, Model> mModels = new LinkedHashMap<>();
    private Map<String, Controller> mControllers = new LinkedHashMap<>();
    private Map<String, View> mViews = new LinkedHashMap<>();
    private Map<String, Sink> mSink = new LinkedHashMap<>();
    private Map<String, Source> mSource = new LinkedHashMap<>();
    private Platform mPlatform;


    public Space(String name){
        super(name);
    }

    /** add components to the particular space */
    public void add(Model m) { this.mModels.put(m.getName(), m); }
    public void add(Controller c) { this.mControllers.put(c.getName(), c); }
    public void add(View v) { this.mViews.put(v.getName(), v); }
    public void add(Sink s) { this.mSink.put(s.getName(), s); }
    public void add(Source v) { this.mSource.put(v.getName(), v); }


    public String getTransmitFunction(){
        return "random_char_update";
    }

    public String getService(){
        return "ravel_service";
    }


    public void add(Platform build) {
        mPlatform = build;
    }
}
