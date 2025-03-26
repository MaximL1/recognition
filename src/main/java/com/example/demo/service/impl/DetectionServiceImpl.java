package com.example.demo.service.impl;

import com.example.demo.model.Detection;
import com.example.demo.model.enums.ObjectType;
import com.example.demo.repository.DetectionCustomRepository;
import com.example.demo.service.DetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetectionServiceImpl implements DetectionService {

	private final DetectionCustomRepository detectionRepository;

	public Page<Detection> getDetections(String startTime, String endTime, Double latitude,
										 Double longitude, Double radiusInKilometers,
										 ObjectType type, String sortBy, String order,
										 Pageable pageable) {

		return detectionRepository.findDetections(startTime, endTime, latitude,
														longitude, radiusInKilometers,
														type, sortBy, order, pageable);
	}
}
