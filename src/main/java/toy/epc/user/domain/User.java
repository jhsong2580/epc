package toy.epc.user.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.epc.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL)
    private Location location;

    @NotEmpty(message = "must not empty")
    private String identification;

    @NotEmpty(message = "must not empty")
    private String password;

    @NotEmpty(message = "must not empty")
    private String email;

    @NotEmpty(message = "must not empty")
    private String handPhone;

    @NotEmpty(message = "must not empty")
    @Enumerated(value = STRING)
    private Power power;

    public User(Location location, String identification, String password, String email,
        String handPhone, Power power) {
        this.location = location;
        this.identification = identification;
        this.password = password;
        this.email = email;
        this.handPhone = handPhone;
        this.power = power;
    }
}
