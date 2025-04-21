package com.auth.authservice.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity()
@Table(name = "\"user\"")
public class UserEntity extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
	@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "username", nullable = false, length = Integer.MAX_VALUE)
	private String username;

	@Column(name = "first_name", nullable = false, length = Integer.MAX_VALUE)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = Integer.MAX_VALUE)
	private String lastName;

	@Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
	private String password;

	@Column(name = "email", nullable = false, unique = true, length = Integer.MAX_VALUE)
	private String email;

	@Column(name = "phone", length = Integer.MAX_VALUE)
	private String phone;

	@Column(name = "profile_image_path", length = Integer.MAX_VALUE)
	private String profileImagePath;

	@Column(name = "active", nullable = false)
	private boolean active;

	@ManyToMany
	private Set<RoleEntity> roles;
}
