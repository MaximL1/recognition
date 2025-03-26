package com.example.demo.service;

import com.example.demo.model.Detection;
import com.example.demo.model.enums.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetectionService {

	Page<Detection> getDetections(String startTime, String endTime, Double latitude,
								  Double longitude, Double radiusInKilometers,
								  ObjectType type, String sortBy, String order,
								  Pageable pageable);
}

