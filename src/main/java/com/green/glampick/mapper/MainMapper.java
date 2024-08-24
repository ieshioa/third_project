package com.green.glampick.mapper;

import com.green.glampick.dto.object.GlampingPriceItem;
import com.green.glampick.dto.object.main.MainGlampingItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    List<MainGlampingItem> popular();
    List<MainGlampingItem> petFriendly();
    List<MainGlampingItem> mountainView();
}
