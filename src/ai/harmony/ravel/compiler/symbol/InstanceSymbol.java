package ai.harmony.ravel.compiler.symbol;

import ai.harmony.antlr4.RavelParser;
import ai.harmony.ravel.compiler.symbol.BaseSymbol;
import ai.harmony.ravel.compiler.symbol.Type;
import ai.harmony.ravel.compiler.symbol.TypedSymbol;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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