package com.tjb.security;

import com.tjb.security.util.JdbcUtil;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.NamingException;
import java.sql.SQLException;

@Singleton
@Startup
public class ApplicationStartup {
    private static final Logger LOG = Logger.getLogger(ApplicationStartup.class);

    @EJB
    JdbcUtil jdbc;

    @PostConstruct
    public void initialize() throws SQLException, NamingException {
        //jdbc.fillUserUUID();
        //jdbc.fillRoleUUID();
    }

    @PreDestroy
    public void terminate() {
    }
}