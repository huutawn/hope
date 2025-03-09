package com.llt.hope.mapper;

import org.mapstruct.Mapper;

import com.llt.hope.dto.request.PermissionRequest;
import com.llt.hope.dto.response.PermissionResponse;
import com.llt.hope.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
