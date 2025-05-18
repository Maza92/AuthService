package com.auth.authservice.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity()
@Table(name = "password_reset_codes")
public class ResetCodeEntity extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_code_id_seq")
	@SequenceGenerator(name = "reset_code_id_seq", sequenceName = "reset_code_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "code", nullable = false, length = 6)
	private String code;

	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;

	@Column(name = "attempts")
	private Integer attempts = 0;

	@Column(name = "used")
	private Boolean used = false;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	public boolean isUsed() {
		return Boolean.TRUE.equals(used);
	}
}
