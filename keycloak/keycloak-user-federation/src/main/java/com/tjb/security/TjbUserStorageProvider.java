package com.tjb.security;

import com.tjb.security.credential.BCryptPasswordEncoder;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * @author Javad Sabbagh
 */
@Stateful
@Local(TjbUserStorageProvider.class)
public class TjbUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator,
        OnUserCache {
    private static final Logger LOG = Logger.getLogger(TjbUserStorageProvider.class);
    public static final String PASSWORD_CACHE_KEY = UserAdapter.class.getName() + ".password";
    private static final int PAGE_SIZE = 20;

    @PersistenceContext
    protected EntityManager em;

    protected ComponentModel model;
    transient protected KeycloakSession session;

    public void setModel(ComponentModel model) {
        this.model = model;
    }

    public void setSession(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void preRemove(RealmModel realm) {

    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {

    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {

    }

    @Remove
    @Override
    public void close() {
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        LOG.debug("getUserById: " + id);
        String persistenceId = StorageId.externalId(id);
        UserEntity user = em.find(UserEntity.class, persistenceId);
        Organ organ = em.find(Organ.class, user.getOrganId());
        if (user == null) {
            LOG.error(String.format("could not find user by id (%s): ", id));
            return null;
        }
        return new UserAdapter(session, realm, model, user, organ != null ? organ.getCode() : null, organ != null ? organ.getServerMac() : null);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        LOG.debug("getUserByUsername: " + username);
        TypedQuery<UserEntity> query = em.createNamedQuery("getUserByUsername", UserEntity.class);
        query.setParameter("username", username);
        List<UserEntity> users = query.getResultList();
        if (users.isEmpty()) {
            LOG.error(String.format("could not find user by Username (%s): ", username));
            return null;
        }
        Organ organ = em.find(Organ.class, users.get(0).getOrganId());
        return new UserAdapter(session, realm, model, users.get(0), organ != null ? organ.getCode() : null, organ != null ? organ.getServerMac() : null);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        TypedQuery<UserEntity> query = em.createNamedQuery("getUserByEmail", UserEntity.class);
        query.setParameter("email", email);
        List<UserEntity> users = query.getResultList();
        if (users.isEmpty()) return null;
        Organ organ = em.find(Organ.class, users.get(0).getOrganId());
        return new UserAdapter(session, realm, model, users.get(0), organ != null ? organ.getCode() : null, organ != null ? organ.getServerMac() : null);
    }

    // todo add from keycloak is not active!
    @Override
    public UserModel addUser(RealmModel realm, String username) {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(username);
        em.persist(user);
        LOG.debug("added user: " + username);
        return new UserAdapter(session, realm, model, user, null, null);
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel userModel) {
        String persistenceId = StorageId.externalId(userModel.getId());
        UserEntity user = em.find(UserEntity.class, persistenceId);
        if (user == null) return false;
        em.remove(user);
        return true;
    }

    @Override
    public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
        String password = ((UserAdapter) delegate).getPassword();
        if (password != null) {
            user.getCachedWith().put(PASSWORD_CACHE_KEY, password);
        }
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return CredentialModel.PASSWORD.equals(credentialType);
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        UserCredentialModel cred = (UserCredentialModel) input;
        UserAdapter adapter = getUserAdapter(user);

        /* Encode credential */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(cred.getValue());

        adapter.setPassword(password);

        return true;
    }

    public UserAdapter getUserAdapter(UserModel userModel) {
        UserAdapter adapter;
        if (userModel instanceof CachedUserModel) {
            adapter = (UserAdapter) ((CachedUserModel) userModel).getDelegateForUpdate();
        } else {
            adapter = (UserAdapter) userModel;
        }
        return adapter;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType)) return;

        getUserAdapter(user).setPassword(null);

    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel userModel) {
        if (getUserAdapter(userModel).getPassword() != null) {
            Set<String> set = new HashSet<>();
            set.add(CredentialModel.PASSWORD);
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel userModel, String credentialType) {
        return supportsCredentialType(credentialType) && getPassword(userModel) != null;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel userModel, CredentialInput input) {
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        UserCredentialModel cred = (UserCredentialModel) input;
        String password = getPassword(userModel);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return password != null && passwordEncoder.matches(cred.getValue(), password);
    }

    public String getPassword(UserModel userModel) {
        String password = null;
        if (userModel instanceof CachedUserModel) {
            password = (String) ((CachedUserModel) userModel).getCachedWith().get(PASSWORD_CACHE_KEY);
        } else if (userModel instanceof UserAdapter) {
            password = ((UserAdapter) userModel).getPassword();
        }
        return password;
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        Object count = em.createNamedQuery("getUserCount")
                .getSingleResult();
        return ((Number) count).intValue();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return getUsers(realm, 0, PAGE_SIZE);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        TypedQuery<UserEntity> query = em.createNamedQuery("getAllUsers", UserEntity.class);
        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }
        List<UserEntity> users = query.getResultList();
        List<UserModel> userModelList = new LinkedList<>();
        for (UserEntity user : users) {
            Organ organ = em.find(Organ.class, user.getOrganId());
            userModelList.add(new UserAdapter(session, realm, model, user, organ != null ? organ.getCode() : null, organ != null ? organ.getServerMac() : null));
        }
        return userModelList;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return searchForUser(search, realm, 0, PAGE_SIZE);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        TypedQuery<UserEntity> query = em.createNamedQuery("searchForUser", UserEntity.class);
        query.setParameter("search", "%" + search.toLowerCase() + "%");
        if (firstResult != -1) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != -1) {
            query.setMaxResults(maxResults);
        }
        List<UserEntity> results = query.getResultList();
        List<UserModel> userModels = new LinkedList<>();
        for (UserEntity user : results) {
            Organ organ = em.find(Organ.class, user.getOrganId());
            userModels.add(new UserAdapter(session, realm, model, user, organ != null ? organ.getCode() : null, organ != null ? organ.getServerMac() : null));
        }
        return userModels;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        LOG.debug("searchForUser 1: " + realm.getName() + " by params:");
        params.entrySet().stream().forEach(e -> {
            LOG.debug(String.format("%s : %s", e.getKey(), e.getValue()));
        });
        LOG.debug("First result: 0");
        LOG.debug("Max result: " + PAGE_SIZE);
        return searchForUser(params.getOrDefault("username", ""), realm, 0, PAGE_SIZE);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        LOG.debug("searchForUser 2: " + realm.getName() + " by params:");
        params.entrySet().stream().forEach(e -> {
            LOG.debug(String.format("%s : %s", e.getKey(), e.getValue()));
        });
        LOG.debug("First result: " + firstResult);
        LOG.debug("Max result: " + maxResults);
        return searchForUser(params.getOrDefault("username", ""), realm, firstResult, maxResults);
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        LOG.warn("This search method is not implemented!");
        LOG.warn("searchForUser by group (first-max result): realm -> " + realm.getName() + " , group -> " + group.getName());
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        LOG.warn("This search method is not implemented!");
        LOG.warn("searchForUser by group: realm -> " + realm.getName() + " , group -> " + group.getName());
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        LOG.warn("This search method is not implemented!");
        LOG.warn("searchForUser: " + realm.getName() + " returns empty list");
        return Collections.EMPTY_LIST;
    }
}
