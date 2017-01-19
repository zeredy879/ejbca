/*************************************************************************
 *                                                                       *
 *  CESeCore: CE Security Core                                           *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public           *
 *  License as published by the Free Software Foundation; either         *
 *  version 2.1 of the License, or any later version.                    *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/

package org.cesecore.roles.member;

import java.io.Serializable;
import java.security.InvalidParameterException;

import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.cesecore.authorization.user.matchvalues.AccessMatchValue;
import org.cesecore.dbprotection.ProtectedData;
import org.cesecore.dbprotection.ProtectionStringBuilder;

/**
 * Entity bean for Role members. Does not correspond to a physical entity, but rather to an individual credential linked to an entity. The same 
 * individuals may share the same credential (such as belonging to the same organization, or sharing an account, while one individual may have access
 * to several credentials (such as a user using several different certificates for identification depending on location). 
 * 
 *  Each member is linked to a Role, though not intrinsically so via foreign keys 
 * 
 * @version $Id$
 * 
 */
@Entity
@Table(name = "RoleMemberData")
public class RoleMemberData extends ProtectedData implements Serializable, Comparable<RoleMemberData> {

    private static final long serialVersionUID = 1L;
   
    private Integer primaryKey;

    private Integer matchValue;
    private String tokenType;
    private String value;
    private Integer roleId;
    
    private String memberBindingType;
    private String memberBinding;
    
    private int rowVersion = 0;
    private String rowProtection;

    public RoleMemberData() {
        
    }
    
    /**
     * Constructor for a RoleMemberData object. Will by default be constructed with the primary key 0, which means that this object hasn't been
     * persisted yet. In that case, the primary key will be set by the CRUD bean. 
     * 
     * @param matchValue the AccessMatchValue to match this object with, i.e CN, SN, etc. 
     * @param value the actual value with which to match
     * @param roleId roleId the ID of the role to which this member belongs. May be null. 
     * @param memberBindingType the type of member binding used for this member. May be null.
     * @param memberBinding the member binding for this member. May be null.
     */
    public RoleMemberData(final AccessMatchValue matchValue, final String value, final Integer roleId, String memberBindingType, String memberBinding) {
        this(null, matchValue, value, roleId, memberBindingType, memberBinding);
    }

    /**
     * Constructor for a RoleMemberData object.
     * 
     * @param primaryKey the primary key for this object. It's required to check the database for any objects with the same key, otherwise that 
     *  object will be overridden
     * @param matchValue the AccessMatchValue to match this object with, i.e CN, SN, etc. 
     * @param value the actual value with which to match
     * @param roleId the ID of the role to which this member belongs. May be null. 
     * @param memberBindingType the type of member binding used for this member. May be null.
     * @param memberBinding the member binding for this member. May be null.
     */
    public RoleMemberData(final Integer primaryKey, final AccessMatchValue matchValue, final String value,
            final Integer roleId, String memberBindingType, String memberBinding) {
        this.primaryKey = primaryKey;
        this.matchValue = matchValue.getNumericValue();
        this.tokenType = matchValue.getTokenType();
        this.value = value;
        this.roleId = roleId;
        this.memberBindingType = memberBindingType;
        this.memberBinding = memberBinding;
    }

    /**
     * @return the primary key of this entity bean, a pseudo-random integer
     */
    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the match value type with to match, i.e. CN, serial number, or username
     */
    public int getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(Integer matchValue) {
        if (matchValue == null) {
            throw new InvalidParameterException("Invalid to set matchValue == null");
        }
        this.matchValue = matchValue;
    }
        
    /**
     * @return a string defining the type of member marker, which is a method to bidn
     */
    public String getMemberBindingType() {
        return memberBindingType;
    }

    public void setMemberBindingType(String memberBindingType) {
        this.memberBindingType = memberBindingType;
    }

    public String getMemberBinding() {
        return memberBinding;
    }

    public void setMemberBinding(String memberBinding) {
        this.memberBinding = memberBinding;
    }

    /**
     * 
     * @return the actual value with which we match
     */
    public String getValue() {
        return value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * 
     * @return the role to which this member belongs. May be null.
     */
    public Integer getRoleId() {
        return roleId;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the authentication token type that this member identifies to (such as X509CertificateAuthenticationToken)
     */
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(final String tokenType) {
        this.tokenType = tokenType;
    }


    public int getRowVersion() {
        return rowVersion;
    }


    public void setRowProtection(final String rowProtection) {
        this.rowProtection = rowProtection;
    }
    

    
    public void setRowVersion(final int rowVersion) {
        this.rowVersion = rowVersion;
    }

    public String getRowProtection() {
        return rowProtection;
    }


    // Start Database integrity protection methods
    @Transient
    @Override
    protected String getProtectString(final int version) {
        final ProtectionStringBuilder build = new ProtectionStringBuilder();
        // What is important to protect here is the data that we define
        // rowVersion is automatically updated by JPA, so it's not important, it is only used for optimistic locking
        build.append(getPrimaryKey()).append(getMatchValue()).append(getValue()).append(getMatchValue());
        return build.toString();
    }

    @Transient
    @Override
    protected int getProtectVersion() {
        return 1;
    }

    @PrePersist
    @PreUpdate
    @Override
    protected void protectData() {
        super.protectData();
    }

    @PostLoad
    @Override
    protected void verifyData() {
        super.verifyData();
    }

    @Override
    @Transient
    protected String getRowId() {
        return String.valueOf(getPrimaryKey());
    }

    //
    // End Database integrity protection methods
    //

    @Override
    public int compareTo(RoleMemberData o) {
        return new CompareToBuilder().append(this.matchValue, o.matchValue).append(this.value, o.value).toComparison();
    }
}