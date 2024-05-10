package com.foxminded.dto.pageimpl;

import com.foxminded.dto.ModelDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

public class ModelPage extends PageImpl<ModelDto> {
    public ModelPage(List<ModelDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
