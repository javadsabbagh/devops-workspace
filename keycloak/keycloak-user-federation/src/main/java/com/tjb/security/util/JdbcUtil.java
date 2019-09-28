package com.tjb.security.util;

import com.tjb.security.TjbRole;
import org.jboss.logging.Logger;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static java.util.Collections.EMPTY_SET;

@Stateless
@Local(JdbcUtil.class)
public class JdbcUtil {

    private static final Logger LOG = Logger.getLogger(JdbcUtil.class);

    @Resource(lookup = "java:jboss/datasources/Clear_Develop_DS")
    private DataSource dataSource;

    public Set<RoleModel> getUserRoles(String userId, RealmModel realmModel) {
        /*
         * Return only item types 2 (of type service) that are assigned to the user.
         * */
        String query = "SELECT TB_ROLE.KEYCLOAK_ID, TB_ROLE.NAME, TB_ROLE.FARSI_NAME\n" +
                "FROM TB_USER\n" +
                "       inner JOIN TB_AUTHORITY on TB_USER.ID = TB_AUTHORITY.USER_ID and TB_AUTHORITY.STATUS_CODE = 1\n" +
                "       inner join TB_ROLE on TB_ROLE.ID = TB_AUTHORITY.ROLE_ID and TB_ROLE.STATUS_CODE = 1\n" +
                "WHERE TB_USER.KEYCLOAK_ID = '" + userId + "' ";

        LOG.debug("Fetching user roles: ");
        LOG.debug(query);

        /**
         * Note:
         * Connection should be explicitly closed which returns it to the connection-pool, instead of physically disconnecting
         * from the database.
         * */
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            Set<RoleModel> roles = new HashSet<>();
            while (rs.next()) {
                String keycloakId = rs.getString("KEYCLOAK_ID");
                String roleName = rs.getString("NAME");
                String description = rs.getString("FARSI_NAME");
                RoleModel role = new TjbRole(realmModel, keycloakId, roleName, description);
                roles.add(role);
                LOG.trace("User role: " + role);
            }
            rs.close();
            stmt.close();
            return roles;
        } catch (Exception exp) {
            LOG.error("----------------------------------------------------------------");
            LOG.error("Error in getting users role: ");
            LOG.error("", exp);
            LOG.error("----------------------------------------------------------------");
            return EMPTY_SET;
        }
    }

    public void fillUserUUID() throws NamingException, SQLException {
        Map<String, String> userMap = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TB_USER");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("KEYCLOAK_ID");
                String userName = rs.getString("USER_NAME");
                if (id == null) {
                    LOG.warn(String.format("Null Keycloak id for user (%s).", userName));
                    String keycloakId = UUID.randomUUID().toString();
                    userMap.put(userName, keycloakId);
                    LOG.info(String.format("Setting Keycloak id (%s) for user (%s).", keycloakId, userName));
                }
            }
            rs.close();
            stmt.close();

            Statement updateStatement = conn.createStatement();
            userMap.entrySet().forEach(e -> {
                try {
                    LOG.info(String.format("UPDATE TB_USER SET KEYCLOAK_ID = '%s' WHERE USER_NAME = '%s'", e.getValue(), e.getKey()));
                    updateStatement.addBatch(String.format("UPDATE TB_USER SET KEYCLOAK_ID = '%s' WHERE USER_NAME = '%s'", e.getValue(), e.getKey()));
                } catch (SQLException exp) {
                    LOG.error("", exp);
                }
            });
            updateStatement.executeBatch();
            updateStatement.close();
        } catch (Exception exp) {
            LOG.error("Error in filling User's Keycloak id: ");
            LOG.error("", exp);
        }
    }

    public void fillRoleUUID() {
        Map<String, String> rolesMap = new HashMap<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TB_ITEM");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("KEYCLOAK_ID");
                String roleName = rs.getString("NAME");
                if (id == null) {
                    LOG.warn(String.format("Null keycloak id for role (%s).", roleName));
                    String keycloakId = UUID.randomUUID().toString();
                    rolesMap.put(roleName, keycloakId);
                    LOG.info(String.format("Setting Keycloak id (%s) for role (%s).", keycloakId, roleName));
                }
            }
            rs.close();
            stmt.close();

            Statement updateStatement = conn.createStatement();
            rolesMap.entrySet().forEach(e -> {
                try {
                    LOG.info(String.format("UPDATE TB_ITEM SET KEYCLOAK_ID = '%s' WHERE NAME = '%s'", e.getValue(), e.getKey()));
                    updateStatement.addBatch(String.format("UPDATE TB_ITEM SET KEYCLOAK_ID = '%s' WHERE NAME = '%s'", e.getValue(), e.getKey()));
                } catch (SQLException exp) {
                    LOG.error("", exp);
                }
            });
            updateStatement.executeBatch();
            updateStatement.close();
        } catch (Exception exp) {
            LOG.error("Error in filling Keycloak id of roles: ");
            LOG.error("", exp);
        }
    }
}
