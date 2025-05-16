package com.example.MiniEvent.adapter.web.dto.mapper;

import com.example.MiniEvent.adapter.web.dto.AppUserDTO;
import com.example.MiniEvent.model.entity.AppUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO fromAppUser(AppUser appUser);
}
