package org.stanford.ravel.primitives;

/**
 * A source that has been linked to a Space (whose parameters are set, if any)
 *
 * Created by gcampagn on 1/30/17.
 */
public class InstantiatedSource extends BaseEventComponent<SourceEvent> {
    InstantiatedSource(Space space, Source source, String varName) {
        super(space, source, varName, SourceEvent.values());
    }
}
