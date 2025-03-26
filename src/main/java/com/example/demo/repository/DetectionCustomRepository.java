package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.Detection;
import com.example.demo.model.enums.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class  DetectionCustomRepository {

	//Create index for search by geolocation
	// db.detections.createIndex({ geoLocation: "2dsphere" })

	private final MongoTemplate mongoTemplate;

	public DetectionCustomRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public Page<Detection> findDetections(String startTime, String endTime, Double latitude,
										  Double longitude, Double radiusInKilometers,
										  ObjectType type, String sortBy, String order,
										  Pageable pageable) {
		Query query = new Query();

		if (startTime != null && endTime != null) {
			query.addCriteria(Criteria.where("timestamp").gte(startTime).lte(endTime));
		}

		if (latitude != null && longitude != null && radiusInKilometers != null) {
			double radiusInRadians = radiusInKilometers / 6378.1;
			query.addCriteria(Criteria.where("geoLocation")
									  .withinSphere(new Circle(longitude, latitude, radiusInRadians)));
		}

		if (type != null) {
			query.addCriteria(Criteria.where("type").is(type));
		}

		if (sortBy != null) {
			query.with(Sort.by(
					order != null && order.equalsIgnoreCase("asc") ?
							Sort.Order.asc(sortBy) :
							Sort.Order.desc(sortBy)
			));
		}

		query.with(pageable);

		List<Detection> detections = mongoTemplate.find(query, Detection.class);

		long count = mongoTemplate.count(query.skip(-1).limit(-1), Detection.class);

		return PageableExecutionUtils.getPage(detections, pageable, () -> count);
	}
}

