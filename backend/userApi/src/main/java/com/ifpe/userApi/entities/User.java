package com.ifpe.userApi.entities;

import com.ifpe.userApi.util.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "BirthDate cannot be null")
    @Past(message = "BirthDate must be in the past")
    private LocalDate birthDate;  // Usando LocalDate em vez de String para representar datas

    @NotBlank(message = "CPF cannot be blank")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF must contain 11 digits")
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotBlank
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    private String address;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull(message="pictureURL cannot be null")
    @Lob
    private String pictureURL;

    @NotNull(message="status cannot be null")
    private Boolean isAccountActive;
}
