package com.auth.authservice.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseAuditEntity {

	@Column(name = "created_at", nullable = false, updatable = false)
	protected Instant createdAt;

	@Column(name = "updated_at")
	protected Instant updatedAt;

	@PrePersist()
	public void prePersist() {
		this.createdAt = Instant.now();
	}

	@PreUpdate()
	public void preUpdate() {
		this.updatedAt = Instant.now();
	}
}
