package com.green.glampick.common;

import jakarta.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Role.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for Role: " + dbData, e);
        }
    }

}
