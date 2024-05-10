package com.foxminded.dto.pageimpl;

import com.foxminded.dto.CarDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CarPage extends PageImpl<CarDto> {
    public CarPage(List<CarDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
