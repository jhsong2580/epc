package toy.epc.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import toy.epc.user.domain.User;

public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentification(String identification);
}


