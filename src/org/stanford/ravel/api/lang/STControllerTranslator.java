package org.stanford.ravel.api.lang;

import org.stanford.ravel.api.builder.FileObject;
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
    private final STGroup irGroup;
    private final TypeFormatter typeFormatter;
    private final LiteralFormatter literalFormatter;

    protected STControllerTranslator(List<FileConfig> controllerFiles, STGroup irGroup, TypeFormatter typeFormatter, LiteralFormatter literalFormatter) {
        this.controllerFiles = controllerFiles;
        this.irGroup = irGroup;
        this.typeFormatter = typeFormatter;
        this.literalFormatter = literalFormatter;
    }

    @Override
    public CodeModule translate(Controller ctr) {
        List<ConcreteEventHandler> eventHandlers = new ArrayList<>();

        for (EventHandler event : ctr) {
            String name = event.getEvent().toString();
            Type model = event.getModelVar().getType();
            String modelName = model.getName();

            STIRTranslator translator = new STIRTranslator(irGroup, typeFormatter, literalFormatter);
            translator.translate(event.getBody());

            ConcreteEventHandler handler = new ConcreteEventHandler(name, modelName, translator.getCode());
            eventHandlers.add(handler);
        }

        CodeModule module = new CodeModule();
        for (FileConfig fc : controllerFiles) {
            ST tmpl = fc.tmpl;
            tmpl.add("eventHandlers", eventHandlers);
            FileObject fo = new FileObject();
            fo.setFileName(fc.fileName);
            fo.setContent(tmpl.render());
            module.addFile(fo);
        }
        return module;
    }
}
