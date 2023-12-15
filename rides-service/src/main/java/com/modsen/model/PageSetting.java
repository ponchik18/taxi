package com.modsen.model;

import com.modsen.constants.RidesServiceConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSetting {
    private int pageNumber = RidesServiceConstants.DefaultValue.PAGE;
    private int elementsPerPage = RidesServiceConstants.DefaultValue.ELEMENT_PER_PAGE;
    private String direction = RidesServiceConstants.DefaultValue.SORT_DESC;
    private String sortField;
}