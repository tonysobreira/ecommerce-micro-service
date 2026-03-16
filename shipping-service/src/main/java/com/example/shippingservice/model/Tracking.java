package com.example.shippingservice.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tracking")
public class Tracking {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "shipment_id", nullable = false)
	private UUID shipmentId;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "location")
	private String location;

	@CreationTimestamp
	@Column(name = "event_at", nullable = false)
	private Instant eventAt;

	public Tracking() {
	}

	public Tracking(UUID shipmentId, String status, String location) {
		this.shipmentId = shipmentId;
		this.status = status;
		this.location = location;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(UUID shipmentId) {
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
