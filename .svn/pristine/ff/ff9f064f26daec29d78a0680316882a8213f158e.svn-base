package com.eazytec.service;

import java.util.List;

import com.eazytec.model.Role;

/**
 * Web Service interface so hierarchy of Generic Manager isn't carried through.
 * @author madan
 */
public interface RoleService {
    /**
     * Retrieves a role by roleId.  An exception is thrown if role not found
     *
     * @param roleId the identifier for the role
     * @return Role
     */
    Role getRole(String roleId);

    /**
     * Finds a role by their rolename.
     *
     * @param rolename the role's rolename used to login
     * @return Role a populated role object
     */
    Role getRoleByName(String rolename);

    /**
     * Retrieves a list of all roles.
     *
     * @return List
     */
    List<Role> getRoles();

    /**
     * Saves a role's information
     *
     * @param role the role's information
     * @return updated role
     * @throws RoleExistsException thrown when role already exists
     */
    Role saveRole(Role role) throws RoleExistsException;

    /**
     * Removes a role from the database by their roleId
     *
     * @param roleId the role's id
     */
    void removeRole(String roleId);
}
