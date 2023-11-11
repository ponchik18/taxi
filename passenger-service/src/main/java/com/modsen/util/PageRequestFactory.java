package com.modsen.util;

import com.modsen.constants.PassengerServiceConstants;
import com.modsen.model.PageSetting;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class PageRequestFactory {
    public static PageRequest buildPageRequest(PageSetting pageSetting) {
        Sort sort = buildSort(pageSetting);
        return PageRequest.of(pageSetting.getPage(), pageSetting.getElementPerPage(), sort);
    }

    private static Sort buildSort(PageSetting pageSetting) {
        if(Objects.nonNull(pageSetting.getKey())) {
            if (pageSetting.getDirection().equals(PassengerServiceConstants.DefaultValue.SORT_ASC)) {
                return Sort.by(pageSetting.getKey()).ascending();
            }
            return Sort.by(pageSetting.getKey()).descending();
        } else{
            return Sort.unsorted();
        }
    }
}