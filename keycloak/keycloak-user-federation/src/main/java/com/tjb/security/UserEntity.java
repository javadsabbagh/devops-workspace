package com.tjb.security;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:kodnuts@gmail.com">Javad Sabbagh</a>
 */

@NamedQueries({
        @NamedQuery(name = "getUserByUsername", query = "select u from UserEntity u where u.username = :username and (u.status is null or u.status = 1)"),
        @NamedQuery(name = "getUserByEmail", query = "select u from UserEntity u where u.email = :email and (u.status is null or u.status = 1)"),
        @NamedQuery(name = "getUserCount", query = "select count(u) from UserEntity u where (u.status is null or u.status = 1)"),
        @NamedQuery(name = "getAllUsers", query = "select u from UserEntity u where (u.status is null or u.status = 1)"),
        @NamedQuery(name = "searchForUser", query = "select u from UserEntity u where " +
                "( lower(u.username) like :search or u.email like :search ) and (u.status is null or u.status = 1) order by u.username"),
})
@Entity
@Table(name = "TB_USER")
public class UserEntity {
    @Id
    @Column(name = "KEYCLOAK_ID")
    private String id;
    /* The id in clearing system. */
    @Column(name = "ID", updatable = false, nullable = false)
    private Long userId;
    @Column(name = "USER_NAME")
    private String username;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "NAME")
    private String name;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "STATUS_CODE")
    private int status;
    @Column(name = "LAST_DATE_CHANGE")
    private LocalDateTime lastDateChange;
    @Column(name = "LAST_USER_ID_CHANGE")
    private Long lastUserIdChange;
    @Column(name = "ORGAN_ID")
    private Long organId;
    @Column(name = "PERSONAL_CODE")
    private Integer personalCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long clearId) {
        this.userId = clearId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getLastDateChange() {
        return lastDateChange;
    }

    public void setLastDateChange(LocalDateTime lastDateChange) {
        this.lastDateChange = lastDateChange;
    }

    public Long getLastUserIdChange() {
        return lastUserIdChange;
    }

    public void setLastUserIdChange(Long lastUserIdChange) {
        this.lastUserIdChange = lastUserIdChange;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public Integer getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(Integer personalCode) {
        this.personalCode = personalCode;
    }
}
