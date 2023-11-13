package com.modsen.model;

import com.modsen.constants.PassengerServiceConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSetting {
    private int pageNumber = PassengerServiceConstants.DefaultValue.PAGE;
    private int elementsPerPage = PassengerServiceConstants.DefaultValue.ELEMENT_PER_PAGE;
    private String direction = PassengerServiceConstants.DefaultValue.SORT_DESC;
    private String sortField;
}