package com.tjb.security;

import javax.persistence.*;

@Entity
@Table(name = "TB_ORGAN_CLEAR_USER")
public class ClearUser {
    @Id
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "ORGAN_ID")
    private Long organId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }
}
