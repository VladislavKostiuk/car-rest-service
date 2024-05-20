package com.foxminded.dto.pageimpl;

import com.foxminded.dto.ManufacturerDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ManufacturerPage extends PageImpl<ManufacturerDto> {
    public ManufacturerPage(List<ManufacturerDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
