package com.tjb.security;

import com.tjb.security.util.ContextUtil;
import com.tjb.security.util.JdbcUtil;
import org.jboss.logging.Logger;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author <a href="mailto:kodnuts@gmail.com">Javad Sabbagh</a>
 */
public class UserAdapter extends AbstractUserAdapterFederatedStorage {
    private static final Logger LOG = Logger.getLogger(UserAdapter.class);
    protected UserEntity entity;
    protected String keycloakId;
    protected RealmModel realm;
    protected Long organCode;
    protected String serverMac;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, UserEntity entity, Long organCode, String serverMac) {
        super(session, realm, model);
        this.entity = entity;
        this.keycloakId = StorageId.keycloakId(model, entity.getId());
        this.realm = realm;
        this.organCode = organCode;
        this.serverMac = serverMac;
    }

    public String getPassword() {
        return entity.getPassword();
    }

    public void setPassword(String password) {
        entity.setPassword(password);
    }

    @Override
    public String getUsername() {
        return entity.getUsername();
    }

    @Override
    public void setUsername(String username) {
        entity.setUsername(username);
    }

    @Override
    public String getFirstName() {
        return entity.getFirstName();
    }

    @Override
    public String getLastName() {
        return entity.getLastName();
    }

    @Override
    protected Set<RoleModel> getRoleMappingsInternal() {
        LOG.debug("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[");
        LOG.debug("getRoleMappingsInternal called: ");
        LOG.debug("]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
        JdbcUtil jdbc = ContextUtil.lookupEjb(JdbcUtil.class);
        return jdbc.getUserRoles(this.entity.getId(), this.realm);
    }

    @Override
    public void setEmail(String email) {
        entity.setEmail(email);
    }

    @Override
    public String getEmail() {
        return entity.getEmail();
    }

    @Override
    public String getId() {
        return keycloakId;
    }

    /*
     * Note:
     * set corresponding entity field in the following methods if you want the attribute value changes to be persisted
     * into the database.
     * */


    @Override
    public void setSingleAttribute(String name, String value) {
        super.setSingleAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        super.removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        super.setAttribute(name, values);
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("userId", mapId(entity.getUserId()));
        all.add("organId", mapId(entity.getOrganId()));
        all.add("organCode", mapId(organCode));
        all.add("personalCode", mapId(entity.getPersonalCode()));
        all.add("serverMac", serverMac == null ? "" : serverMac);

//        all.add("locale", "fa-IR");
        return all;
    }

    @Override
    public List<String> getAttribute(String attr) {
        switch (attr) {
            /*
             * Theses attributes are unavailable in Tejarat User Identity system, then return empty string for their values.
             * */
            case "phone":
            case "website":
            case "profile":
            case "picture":
            case "gender":
            case "middleName":
            case "nickname":
            case "zoneinfo":
            case "birthdate":
                return Collections.singletonList("");
            case "locale":
                return Arrays.asList("fa-IR");
                /**
                 * Warning: don't return "updatedAt" attribute: It fails on role authorization.
                 * */
//            case "updatedAt":
//                return Arrays.asList(entity.getLastDateChange().format(DateTimeFormatter.ISO_DATE_TIME));
            case "userId":
                return Collections.singletonList(mapId(entity.getUserId()));
            case "organId":
                return Collections.singletonList(mapId(entity.getOrganId()));
            case "organCode":
                return Collections.singletonList(mapId(this.organCode));
            case "serverMac":
                return Collections.singletonList(this.serverMac == null ? "" : serverMac);
            case "personalCode":
                return Collections.singletonList(mapId(entity.getPersonalCode()));
            default:
                if (super.getAttribute(attr) != null) {
                    return super.getAttribute(attr);
                } else {
                    //LOG.warn("Attribute with " + attr + " wanted, but it is null.");
                    /* prevent null pointer exception, on getting not-existing user attributes. */
                    return new ArrayList<>();
                }
        }
    }

    private String mapId(Number id) {
        if (id == null) {
            return "-1";
        } else {
            return id + "";
        }
    }
}
