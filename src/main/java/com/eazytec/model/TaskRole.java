package com.eazytec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;

/**
 * This class is used to represent available task role in the database.
 *
 * @author nkumar
 */
@Entity
@Table(name = "TASK_ROLE")
@NamedQueries({
        @NamedQuery(
                name = "findTaskRoleByName",
                query = "select r from TaskRole r where r.name = :name "
        )
})
public class TaskRole extends BaseObject implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 3690197650654049848L;
    private String id;
    private String name;
    private String description;

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public TaskRole() {
    }

    /**
     * Create a new instance and set the name.
     *
     * @param name name of the  task role .
     */
    public TaskRole(final String name) {
        this.name = name;
    }

    @Id
    @Column(name = "ID", length=70)    //@GeneratedValue(generator="system-uuid")
    //@GenericGenerator(name="system-uuid", strategy = "uuid")
    
    public String getId() {
        return id;
    }

    /**
     * @return the name property (getAuthority required by Acegi's GrantedAuthority interface)
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
    @Transient
    public String getAuthority() {
        return getName();
    }

    @Column(name = "NAME", length = 40)
    public String getName() {
        return this.name;
    }

    @Column(name = "DESCRIPTION", length = 64)
    public String getDescription() {
        return this.description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskRole)) {
            return false;
        }

        final TaskRole taskRole = (TaskRole) o;

        return !(name != null ? !name.equals(taskRole.name) : taskRole.name != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

}