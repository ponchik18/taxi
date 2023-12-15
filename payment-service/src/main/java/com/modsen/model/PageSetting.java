package com.modsen.model;

import com.modsen.constants.PaymentServiceConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSetting {
    private int pageNumber = PaymentServiceConstants.DefaultValue.PAGE;
    private int elementsPerPage = PaymentServiceConstants.DefaultValue.ELEMENT_PER_PAGE;
    private String direction = PaymentServiceConstants.DefaultValue.SORT_DESC;
    private String sortField;
}