package org.stanford.ravel.rrt;

import java.security.Key;

/**
 * Created by gcampagn on 2/28/17.
 */
public interface KeyProvider {
    Key loadKey(int keyId);
}
