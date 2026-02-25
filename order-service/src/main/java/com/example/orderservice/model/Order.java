package com.example.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(nullable = false)
	private String status;

	@Column(name = "customer_email", nullable = false)
	private String customerEmail;

	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;

	@Column(name = "ship_line1", nullable = false)
	private String shipLine1;

	@Column(name = "ship_line2")
	private String shipLine2;

	@Column(name = "ship_city", nullable = false)
	private String shipCity;

	@Column(name = "ship_state")
	private String shipState;

	@Column(name = "ship_zip", nullable = false)
	private String shipZip;

	@Column(name = "ship_country", nullable = false)
	private String shipCountry;

	@Column(nullable = false)
	private String currency;

	@Column(name = "subtotal_cents", nullable = false)
	private long subtotalCents;

	@Column(name = "shipping_cents", nullable = false)
	private long shippingCents;

	@Column(name = "total_cents", nullable = false)
	private long totalCents;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected Order() {
	}

	public Order(UUID id, UUID userId, String customerEmail, OrderStatus status, PaymentMethod paymentMethod,
			String shipLine1, String shipLine2, String shipCity, String shipState, String shipZip, String shipCountry,
			String currency, long subtotalCents, long shippingCents, long totalCents, Instant createdAt,
			Instant updatedAt) {
		this.id = id;
		this.userId = userId;
		this.status = status.name();
		this.customerEmail = customerEmail;
		this.paymentMethod = paymentMethod.name();
		this.shipLine1 = shipLine1;
		this.shipLine2 = shipLine2;
		this.shipCity = shipCity;
		this.shipState = shipState;
		this.shipZip = shipZip;
		this.shipCountry = shipCountry;
		this.currency = currency;
		this.subtotalCents = subtotalCents;
		this.shippingCents = shippingCents;
		this.totalCents = totalCents;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UUID getId() { return id; }
	public UUID getUserId() { return userId; }
	public String getStatus() { return status; }
	public String getCustomerEmail() { return customerEmail; }
	public String getPaymentMethod() { return paymentMethod; }
	public String getShipLine1() { return shipLine1; }
	public String getShipLine2() { return shipLine2; }
	public String getShipCity() { return shipCity; }
	public String getShipState() { return shipState; }
	public String getShipZip() { return shipZip; }
	public String getShipCountry() { return shipCountry; }
	public String getCurrency() { return currency; }
	public long getSubtotalCents() { return subtotalCents; }
	public long getShippingCents() { return shippingCents; }
	public long getTotalCents() { return totalCents; }
	public Instant getCreatedAt() { return createdAt; }
	public Instant getUpdatedAt() { return updatedAt; }

	public OrderStatus statusEnum() {
		return OrderStatus.valueOf(status);
	}

	public void setStatus(OrderStatus status) {
		this.status = status.name();
		touch();
	}

	public void touch() {
		this.updatedAt = Instant.now();
	}
}
