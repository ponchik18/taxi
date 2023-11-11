package com.modsen.model;

import com.modsen.constants.PassengerServiceConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSetting {
    private Integer page = PassengerServiceConstants.DefaultValue.PAGE;
    private Integer elementPerPage = PassengerServiceConstants.DefaultValue.ELEMENT_PER_PAGE;
    private String direction = PassengerServiceConstants.DefaultValue.SORT_DESC;
    private String key;
}