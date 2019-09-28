package com.tjb.security;

import javax.persistence.*;

@Entity
@Table(name = "TB_ORGAN")
public class Organ {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;
    @Column(name = "CODE", unique = true)
    private Long code;
    @Column(name = "SERVER_MAC")
    private String serverMac;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getServerMac() {
        return serverMac;
    }

    public void setServerMac(String serverMac) {
        this.serverMac = serverMac;
    }
}
