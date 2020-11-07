package com.github.westee.course.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="permission", schema = "public")
public class Permission extends BaseEntity{
    private String name;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Role role;

    @ManyToOne
    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
