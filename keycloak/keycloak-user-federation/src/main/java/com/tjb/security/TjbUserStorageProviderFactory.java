
package com.tjb.security;

import com.tjb.security.util.ContextUtil;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import javax.naming.InitialContext;

/**
 * @author <a href="mailto:kodnuts@gmail.com">Javad Sabbagh</a>
 */
public class TjbUserStorageProviderFactory implements UserStorageProviderFactory<TjbUserStorageProvider> {
    private static final Logger LOG = Logger.getLogger(TjbUserStorageProviderFactory.class);

    @Override
    public TjbUserStorageProvider create(KeycloakSession session, ComponentModel model) {
            TjbUserStorageProvider provider = ContextUtil.lookupEjb(TjbUserStorageProvider.class);
            provider.setModel(model);
            provider.setSession(session);
            return provider;
    }

    @Override
    public String getId() {
        return "tjb-user-federation";
    }

    @Override
    public String getHelpText() {
        return "Tjb User Storage Provider";
    }

    @Override
    public void close() {
        LOG.debug("<<<<<< Closing factory");

    }
}
