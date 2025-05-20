package com.auth.authservice.entity;

import java.time.Instant;

import com.auth.authservice.enums.TokenEnum;
import com.auth.authservice.enums.TokenTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Accessors(chain = true)
@Entity()
@Table(name = "jwt_token")
public class JwtTokenEntity extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwt_token_id_seq")
	@SequenceGenerator(name = "jwt_token_id_seq", sequenceName = "jwt_token_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "token", nullable = false, columnDefinition = "TEXT")
	private String token;

	@Column(name = "token_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TokenTypeEnum tokenType;

	@Column(name = "is_valid", nullable = false)
	private boolean isValid;

	@Column(name = "is_revoked", nullable = false)
	private boolean isRevoked;

	@Column(name = "expiry_date", nullable = false)
	private Instant expiry;

	@Column(name = "revoked_at")
	private Instant revokedAt;

	@Column(name = "refresh_count")
	private Integer refreshCount = 0;

	@Column(name = "last_refresh_at")
	private Instant lastRefreshAt;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@ToString.Exclude
	private UserEntity userEntity;

	@Column(name = "jti", nullable = false, columnDefinition = "TEXT")
	private String jti;
}
