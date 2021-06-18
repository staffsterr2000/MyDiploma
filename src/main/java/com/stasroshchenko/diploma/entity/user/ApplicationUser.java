package com.stasroshchenko.diploma.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@Data
@NoArgsConstructor

@Entity         // вказуємо, що цей клас є сутністю
@Table(name = "application_user")   // назва таблиці

// завдяки цій стратегії наслідування створюються: головна
// таблиця; таблиці її наслідників, у яких буде батьківський
// primary key (у нашому випадку це ID)
@Inheritance(strategy = InheritanceType.JOINED)

// додаємо до таблиці application_user стовпець ROLE
// у якому буде написано якого з наслідницьких типів буде
// наш об'єкт
@DiscriminatorColumn(name = "ROLE")
public abstract class ApplicationUser implements UserDetails {

    // назва sequence для цієї сутності
    private static final String SEQUENCE_NAME = "application_user_sequence";

    @Id         // вказуємо що цей стовпець типу ID
    @SequenceGenerator(     // створюємо sequence
            name = SEQUENCE_NAME,
            sequenceName = SEQUENCE_NAME,
            allocationSize = 1
    )
    @GeneratedValue(        // вказуємо генерацію значень
            strategy = GenerationType.SEQUENCE,
            generator = SEQUENCE_NAME
    )
    @EqualsAndHashCode.Exclude
    protected Long id;

    @Column(nullable = false)   // прибираємо nullable у стовпчику
    protected String username;

    @Column(nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    protected String imageLink = "default.jpg";

    @Column(nullable = false)
    protected boolean isAccountLocked;

    @Column(nullable = false)
    protected boolean isEnabled = false;

    public ApplicationUser(
            String username,
            String email,
            String password
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
