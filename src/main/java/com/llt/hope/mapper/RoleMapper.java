package com.llt.hope.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.llt.hope.dto.request.RoleRequest;
import com.llt.hope.dto.response.RoleResponse;
import com.llt.hope.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
