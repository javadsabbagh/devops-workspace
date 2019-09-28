package com.tjb.security;

import org.jboss.logging.Logger;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;

import java.util.*;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_SET;

public class TjbRole implements RoleModel {
    private static final Logger LOG = Logger.getLogger(RoleModel.class);

    private String name;
    private String id;
    private String description;
    private RealmModel realmModel;

    public TjbRole(RealmModel realmModel, String id, String name, String description) {
        this.realmModel = realmModel;
        this.name = name;
        this.id = id;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public void addCompositeRole(RoleModel role) {

    }

    @Override
    public void removeCompositeRole(RoleModel role) {

    }

    @Override
    public Set<RoleModel> getComposites() {
        return EMPTY_SET;
    }

    @Override
    public boolean isClientRole() {
        return false;
    }

    @Override
    public String getContainerId() {
        return this.realmModel.getId();
    }

    @Override
    public RoleContainerModel getContainer() {
        return this.realmModel;
    }

    /**
     * For composite roles, since we don't have composite roles always return false.
     * */
    @Override
    public boolean hasRole(RoleModel role) {
        LOG.debug("Role " + this.getName() + " hasRole " + role.getName() + " ?");
        return false;
    }

    @Override
    public void setSingleAttribute(String name, String value) {

    }

    @Override
    public void setAttribute(String name, Collection<String> values) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public String getFirstAttribute(String name) {
        return null;
    }

    @Override
    public List<String> getAttribute(String name) {
        return EMPTY_LIST;
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        LOG.debug("-----------------------------------");
        LOG.debug("role getAttributes() called.");
        LOG.debug("-----------------------------------");
        return new HashMap<>();
    }


    @Override
    public String toString() {
        return "TjbRole {" +
                "name:'" + name + '\'' +
                ", id:'" + id + '\'' +
                ", description:'" + description + '\'' +
                '}';
    }
}
