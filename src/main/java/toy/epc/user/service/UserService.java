package toy.epc.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.epc.exception.NoUserException;
import toy.epc.user.domain.User;
import toy.epc.user.repository.JpaUserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository jpaUserRepository;

    public User getUserById(Long id) {
        Optional<User> userOptional = jpaUserRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoUserException("사용자를 찾을수 없습니다");

        }
        return userOptional.get();
    }

    public Optional<User> getUserByIdentification(String identification) {
        return jpaUserRepository.findByIdentification(identification);
    }

}
