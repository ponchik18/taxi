package com.modsen.contoller;

import com.modsen.constants.RatingServiceConstants;
import com.modsen.dto.RatingListResponse;
import com.modsen.dto.RatingRequest;
import com.modsen.dto.RatingResponse;
import com.modsen.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public RatingListResponse getAllRating(@RequestParam(required = false) Long entityId) {
        return ratingService.getAllRatingByEntityId(entityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingResponse createRating(@Valid @RequestBody RatingRequest ratingRequest) {
        return ratingService.createRating(ratingRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse getRatingById(@PathVariable long id) {
        return ratingService.getRatingById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingResponse updateRating(@PathVariable long id, @Valid @RequestBody RatingRequest ratingRequest) {
        return ratingService.updateRating(id, ratingRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable long id) {
        ratingService.deleteRating(id);
    }
}