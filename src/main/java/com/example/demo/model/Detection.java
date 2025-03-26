package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "detections")
public class Detection {
	@Id
	private String uniqueId;
	private String timestamp;
	private String source;
	@GeoSpatialIndexed
	private GeoLocation geoLocation;
	private String type;
	private Double confidence;
}

@Data
class GeoLocation {
	private Double lat;
	private Double lon;
}

