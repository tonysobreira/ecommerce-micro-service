package com.example.orderservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
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
	private BigDecimal subtotalCents;

	@Column(name = "shipping_cents", nullable = false)
	private BigDecimal shippingCents;

	@Column(name = "total_cents", nullable = false)
	private BigDecimal totalCents;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;

	protected Order() {
	}

	public Order(UUID userId, String customerEmail, OrderStatus status, PaymentMethod paymentMethod, String shipLine1,
			String shipLine2, String shipCity, String shipState, String shipZip, String shipCountry, String currency,
			BigDecimal subtotalCents, BigDecimal shippingCents, BigDecimal totalCents) {
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
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getShipLine1() {
		return shipLine1;
	}

	public void setShipLine1(String shipLine1) {
		this.shipLine1 = shipLine1;
	}

	public String getShipLine2() {
		return shipLine2;
	}

	public void setShipLine2(String shipLine2) {
		this.shipLine2 = shipLine2;
	}

	public String getShipCity() {
		return shipCity;
	}

	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}

	public String getShipState() {
		return shipState;
	}

	public void setShipState(String shipState) {
		this.shipState = shipState;
	}

	public String getShipZip() {
		return shipZip;
	}

	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}

	public String getShipCountry() {
		return shipCountry;
	}

	public void setShipCountry(String shipCountry) {
		this.shipCountry = shipCountry;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getSubtotalCents() {
		return subtotalCents;
	}

	public void setSubtotalCents(BigDecimal subtotalCents) {
		this.subtotalCents = subtotalCents;
	}

	public BigDecimal getShippingCents() {
		return shippingCents;
	}

	public void setShippingCents(BigDecimal shippingCents) {
		this.shippingCents = shippingCents;
	}

	public BigDecimal getTotalCents() {
		return totalCents;
	}

	public void setTotalCents(BigDecimal totalCents) {
		this.totalCents = totalCents;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

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
