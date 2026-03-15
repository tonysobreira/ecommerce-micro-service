package com.example.shippingservice.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tracking")
public class Tracking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long shipmentId;

	@Column(nullable = false)
	private String status;

	private String location;

	@Column(nullable = false)
	private Instant eventAt = Instant.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Instant getEventAt() {
		return eventAt;
	}

	public void setEventAt(Instant eventAt) {
		this.eventAt = eventAt;
	}

}
