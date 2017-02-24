package edu.stanford.ravel.presenter;

import edu.stanford.ravel.model.RavelAbstractModel;

/**
 * Created by lauril on 2/8/16.
 */
public interface ModelListener {
    void onModelUpdate(RavelAbstractModel serializedModel);
}
