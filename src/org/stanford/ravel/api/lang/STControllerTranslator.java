package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.FileObject;
import org.stanford.ravel.compiler.symbol.VariableSymbol;
import org.stanford.ravel.compiler.types.Type;
import org.stanford.ravel.primitives.Controller;
import org.stanford.ravel.primitives.EventHandler;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Translate a controller into code using StringTemplates
 *
 * Created by gcampagn on 1/27/17.
 */
public class STControllerTranslator implements ControllerTranslator {
    public static class FileConfig {
        private final String fileName;
        private final ST tmpl;

        public FileConfig(String fileName, ST tmpl) {
            this.fileName = fileName;
            this.tmpl = tmpl;
        }
    }

    private final List<FileConfig> controllerFiles;
    private final IRTranslator irTranslator;

    protected STControllerTranslator(List<FileConfig> controllerFiles, IRTranslator irTranslator) {
        this.controllerFiles = controllerFiles;
        this.irTranslator = irTranslator;
    }

    @Override
    public CodeModule translate(Controller ctr) {
        List<ConcreteEventHandler> eventHandlers = new ArrayList<>();

        for (EventHandler event : ctr) {
            String name = event.getEventType().getEventName();
            Type model = event.getModelVar().getType();
            String modelName = model.getName();

            List<VariableSymbol> arguments = event.getArguments();
            irTranslator.translate(ctr.getParameterSymbols(), arguments, event.getBody());

            ConcreteEventHandler handler = new ConcreteEventHandler(name, modelName, arguments, irTranslator.getCode());
            eventHandlers.add(handler);
        }

        CodeModule module = new CodeModule();
        for (FileConfig fc : controllerFiles) {
            ST tmpl = fc.tmpl;
            tmpl.add("eventHandlers", eventHandlers);
            tmpl.add("parameters", ctr.getParameterSymbols());
            FileObject fo = new FileObject();
            fo.setFileName(fc.fileName);
            fo.setContent(tmpl.render());
            module.addFile(fo);
        }
        return module;
    }
}
