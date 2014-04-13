package org.openflow.protocol.factory;

/**
 * Objects implementing this interface are expected to be instantiated with an
 * instance of an OFMultipartFactory
 * @author David Erickson (daviderickson@cs.stanford.edu)
 */
public interface OFMultipartFactoryAware {
    /**
     * Sets the OFMultipartFactory
     * @param multipartFactory
     */
    public void setMultipartFactory(OFMultipartFactory multipartFactory);
}
