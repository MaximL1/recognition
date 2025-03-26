package com.example.demo.controller;

import com.example.demo.model.Detection;
import com.example.demo.model.enums.ObjectType;
import com.example.demo.service.DetectionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/detections")
@RequiredArgsConstructor
public class DetectionController {

	private final DetectionService detectionService;

	@Operation(
			summary = "Get detections",
			description = "Retrieve object detections with optional filters such as time range, geo-location, object type, and sorting.",
			tags = { "Detections" }
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful retrieval of detections",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Page.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters",
					content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "Internal server error",
					content = @Content(mediaType = "application/json"))
	})
	@GetMapping
	public Page<Detection> getDetections(
			@Parameter(description = "Start of the timestamp range (ISO 8601 format)", example = "2025-03-25T10:00:00Z")
			@RequestParam(required = false) String startTime,

			@Parameter(description = "End of the timestamp range (ISO 8601 format)", example = "2025-03-25T12:00:00Z")
			@RequestParam(required = false) String endTime,

			@Parameter(description = "Latitude of the center for geo-location search", example = "40.7128")
			@RequestParam(required = false) @Min(-90) @Max(90) Double latitude,

			@Parameter(description = "Longitude of the center for geo-location search", example = "-74.0060")
			@RequestParam(required = false) @Min(-180) @Max(180) Double longitude,

			@Parameter(description = "Radius in kilometers for geo-location search", example = "5")
			@RequestParam(required = false) @Min(0) @Max(500) Double radiusInKilometers,

			@Parameter(description = "Object type to filter by (e.g., 'car', 'truck')", example = "truck")
			@RequestParam(required = false) ObjectType type,

			@Parameter(description = "Field to sort by (e.g., 'timestamp', 'confidence')", example = "timestamp")
			@RequestParam(defaultValue = "timestamp") String sortBy,

			@Parameter(description = "Sort order: 'asc' for ascending, 'desc' for descending", example = "asc")
			@RequestParam(defaultValue = "asc") String order,

			@Parameter(description = "Page number for pagination (0-based)", example = "0")
			@RequestParam(defaultValue = "0")  @Min(0) int page,

			@Parameter(description = "Number of records per page", example = "50")
			@RequestParam(defaultValue = "50")  @Min(1) int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		return detectionService.getDetections(startTime, endTime, latitude, longitude, radiusInKilometers, type, sortBy, order, pageable);
	}
}

