package org.stanford.ravel.compiler.symbol;

import org.stanford.antlr4.RavelParser;
import org.stanford.ravel.compiler.types.Type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lauril on 8/26/16.
 */
public class InstanceSymbol extends BaseSymbol implements TypedSymbol {

    private String identifier;
    private String instance_name;
    private Map<String,String>  parameterMap;
    RavelParser.InstanceContext context;

    public InstanceSymbol(String name, RavelParser.InstanceContext ictx) {
        super(name);
        parameterMap = new LinkedHashMap<>();
        identifier = ictx.Identifier().getText();
        instance_name = ictx.instance_name().getText();
        context = ictx;

       // parameterMap = refList;

    }

    public String getIdentifier(){
        return identifier;
    }

    public String getInstanceName(){
        return instance_name;
    }

    public void addParameter(String key, String val){
        parameterMap.put(key, val);
    }
    public Map<String,String> getParameterMap(){
        return parameterMap;
    }
    @Override
    public void setType(Type type) {
        super.setType(type);
    }


}