package org.bayaweaver.domainmodel.domain.model;

public interface Actor {

    <T extends Role> T inRoleOf(Class<T> role) throws IllegalRoleException;
}
