package org.stanford.ravel.rrt;

/**
 * Created by lauril on 1/31/17.
 */
public abstract class AbstractDispatcher implements DispatcherAPI, SystemEventAPI  {


    protected abstract void runNextEvent();
    /***********************************************************************/
    /*************** Callbakcs to the model to AD **************************/
    /***********************************************************************/
    protected abstract void models__notifyDeparted(Event event);

    protected abstract void models__notifyArrived(Event event);
}
