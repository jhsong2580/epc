package toy.epc.user.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Location {

    @Id
    @Column(name = "LOCATION_ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JsonIgnore
    private User user;

    @NotEmpty(message = "city must not empty")
    @Size(min = 1, max = 10, message = "city size 1~10")
    private String city;

    @NotEmpty(message = "gu must not empty")
    @Size(min = 1, max = 10, message = "gu size 1~10")
    private String gu;

    public Location(User user, String city, String gu) {
        this.user = user;
        this.city = city;
        this.gu = gu;
    }
}
