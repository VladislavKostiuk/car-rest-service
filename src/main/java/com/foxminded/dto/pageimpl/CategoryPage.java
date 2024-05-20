package com.foxminded.dto.pageimpl;

import com.foxminded.dto.CategoryDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CategoryPage extends PageImpl<CategoryDto> {
    public CategoryPage(List<CategoryDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
