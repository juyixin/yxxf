package com.eazytec.bpm.model;

import org.activiti.engine.form.FormProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import com.eazytec.model.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * This class is used to represent available roles in the database.
 *
 * @author madan
 */

public class FieldPermission {
    private static final long serialVersionUID = 3690197650654049848L;
    private Role role;
    private FormProperty formProperty;
    private boolean isReadable;
    private boolean isEditable;
    private boolean isWritable;

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public FieldPermission() {
    }

    
}