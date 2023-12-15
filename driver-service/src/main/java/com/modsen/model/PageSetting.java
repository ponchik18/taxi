package com.modsen.model;

import com.modsen.constants.DriverServiceConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSetting {
    private int pageNumber = DriverServiceConstants.DefaultValue.PAGE;
    private int elementsPerPage = DriverServiceConstants.DefaultValue.ELEMENT_PER_PAGE;
    private String direction = DriverServiceConstants.DefaultValue.SORT_DESC;
    private String sortField;
}