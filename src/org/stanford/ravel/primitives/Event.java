package org.stanford.ravel.primitives;

import org.antlr.v4.misc.OrderedHashMap;

import java.util.Map;

/**
 * Returns event hooks in various languages
 * translates loops and access to the variables
 * Created by lauril on 8/16/16.
 */
public class Event{
    protected Controller mController;
    protected Map<String, Variable> mVariables;
    protected Map<String, String> mArgs;
    protected  String mName;

    protected Event() {
        mVariables = new OrderedHashMap<>();
        mArgs = new OrderedHashMap<>();
    }

    public String getName(){ return mName; }



    public static class Builder {
        protected Event mEvent;
        protected Builder mBuilder;

        public Builder() {
            mEvent = new Event();
            mBuilder = this;
        }

        public Event build() {
            return mEvent;
        }

        public Builder name(String n){ mEvent.mName = n ; return mBuilder; }

        public Builder setController(Controller c) {
            mEvent.mController = c;
            return mBuilder;
        }

        //TODO: this can be refactored with the controller
        //Java gurus, chop it on!
        public Builder addVariable(Variable v) {
            mEvent.mVariables.put(v.getName(), v);
            return mBuilder;

        }

        public Builder addArg(String type, String name) {
            mEvent.mArgs.put(type, name);
            return mBuilder;
        }
    }

    @Override
    public String toString(){
        return "Event " + mName + " args: " + mArgs + " vars: " + mVariables;
    }
}
