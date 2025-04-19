package com.example.MiniEvent.adapter.web.dto.mapper;

import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequest;
import com.example.MiniEvent.model.entity.Event;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromRequest(UpdateEventRequest request, @MappingTarget Event event);
}
