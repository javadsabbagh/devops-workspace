package com.tjb.security.util;

import org.jboss.logging.Logger;

import javax.naming.InitialContext;

public class ContextUtil {
    private static final Logger LOG = Logger.getLogger(ContextUtil.class);

    public static <T> T lookupEjb(Class<T> klass) {
        String resourceName = "java:global/tjb-user-federation/" + klass.getSimpleName();
        try {
            InitialContext ctx = new InitialContext();
            return (T) ctx.lookup(resourceName);
        } catch (Exception e) {
            LOG.error("--------------------------------------");
            LOG.error("Error in ejb lookup: " + resourceName);
            throw new RuntimeException(e);
        }
    }
}
