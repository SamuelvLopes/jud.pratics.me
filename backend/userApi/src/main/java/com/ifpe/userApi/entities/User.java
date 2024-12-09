package com.ifpe.userApi.entities;

import com.ifpe.userApi.util.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_user",
        indexes = {
                @Index(name = "idx_user_chat", columnList = "id")
        })

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Setter
    private String name;

    @NotNull(message = "BirthDate cannot be null")
    @Past(message = "BirthDate must be in the past")
    @Setter
    private LocalDate birthDate;

    @NotBlank(message = "CPF cannot be blank")
    @Pattern(regexp = "^[0-9]{11}$", message = "CPF must contain 11 digits")
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotBlank
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    private String address;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    @Setter
    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role cannot be null")
    @Setter
    private Role role;

    @NotNull(message = "pictureURL cannot be null")
    @Setter
    private String pictureURL;

    @NotNull(message = "Status cannot be null")
    @Setter
    private Boolean isAccountActive;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, max = 12, message = "Password must be between 8 and 12 characters")
    @Setter
    private String password;

    @NotBlank
    @Column(unique = true)
    private String uniqueToken;

    @ManyToMany
    @JoinTable(
            name = "user_chat",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private Set<Chat> chats = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "responsible")
    private Set<Post> responsiblePosts = new HashSet<>();
}
