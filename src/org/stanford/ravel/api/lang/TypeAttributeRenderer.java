package org.stanford.ravel.api.lang;

import org.stanford.ravel.compiler.types.Type;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.Locale;

/**
 * Bridge TypeFormatter to ST's AttributeRender
 *
 * Created by gcampagn on 1/31/17.
 */
public class TypeAttributeRenderer implements AttributeRenderer {
    private final TypeFormatter formatter;

    public TypeAttributeRenderer(TypeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return formatter.toNativeType((Type)o);
    }
}
