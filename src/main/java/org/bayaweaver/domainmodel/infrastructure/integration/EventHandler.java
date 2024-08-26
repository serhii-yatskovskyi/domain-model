package org.bayaweaver.domainmodel.infrastructure.integration;

import java.io.Serializable;

public interface EventHandler {

    void handleEvent(Serializable event) throws Exception;
}
