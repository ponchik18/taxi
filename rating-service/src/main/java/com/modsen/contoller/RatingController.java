package com.modsen.contoller;

import com.modsen.constants.RatingServiceConstants;
import com.modsen.dto.rating.RatingListResponse;
import com.modsen.dto.rating.RatingRequest;
import com.modsen.dto.rating.RatingResponse;
import com.modsen.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RatingServiceConstants.Path.RATING_CONTROLLER_PATH)
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingListResponse getAllRating(@RequestParam(required = false) Long entityId, @RequestParam(required = false) String userRole) {
        return ratingService.getAllRatingByEntityId(entityId, userRole);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PASSENGER') || hasRole('')")
    public RatingResponse createRating(@Valid @RequestBody RatingRequest ratingRequest) {
        return ratingService.createRating(ratingRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse getRatingById(@PathVariable long id) {
        return ratingService.getRatingById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable long id) {
        ratingService.deleteRating(id);
    }
}