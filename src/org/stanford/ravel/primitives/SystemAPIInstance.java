package org.stanford.ravel.primitives;

/**
 * The one and only instance of SystemAPI that is always available
 *
 * Created by gcampagn on 2/21/17.
 */
public class SystemAPIInstance extends EventComponentInstance<SystemAPI> {
    public SystemAPIInstance(SystemAPI api, String varName) {
        super(api, api.getSpace(), varName);
    }
}
