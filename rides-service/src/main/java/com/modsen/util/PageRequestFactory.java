package com.modsen.util;

import com.modsen.constants.RidesServiceConstants;
import com.modsen.model.PageSetting;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class PageRequestFactory {
    public static PageRequest buildPageRequest(PageSetting pageSetting) {
        Sort sort = buildSort(pageSetting);
        return PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage(), sort);
    }

    private static Sort buildSort(PageSetting pageSetting) {
        if(Objects.isNull(pageSetting.getSortField())) {
            return Sort.unsorted();
        }

        Sort sort = Sort.by(pageSetting.getDirection());

        if(pageSetting.getDirection().equals(RidesServiceConstants.DefaultValue.SORT_ASC)) {
            return sort.ascending();
        }
        return sort.descending();
    }
}