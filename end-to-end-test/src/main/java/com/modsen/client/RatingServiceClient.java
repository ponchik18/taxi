package com.modsen.client;

import com.modsen.constant.EndToEndTestConstant;
import com.modsen.dto.rating.RatingRequest;
import com.modsen.dto.rating.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = EndToEndTestConstant.ServiceName.RATING_SERVICE_NAME,
        url = "${feign-client.rating-service.url}",
        path = EndToEndTestConstant.ServiceBasePath.RATING_SERVICE_PATH)
public interface RatingServiceClient {
    @PostMapping
    RatingResponse createRating(@RequestBody RatingRequest ratingRequest);

    @GetMapping
    RatingResponse getRatingById(@PathVariable long id);
}