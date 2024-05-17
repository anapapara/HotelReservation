package com.reservation.repository;

import com.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer id);

    @Override
    <S extends User> S save(S entity);

    /**
     * Get user by personal code
     *
     * @param code The personal code of searched user
     * @return Optional of user
     */
    @Query("SELECT u FROM User u WHERE u.personalCode = :code")
    Optional<User> findByPersonalCode(@Param("code") String code);

    /**
     * Find if exists user with given personal code and if not exists, add new user
     *
     * @param user User to search for or the new user
     * @return User that was searched or the new user created
     */
    @Modifying
    @Transactional
    default User login(User user) {
        Optional<User> existingUserOptional = findByPersonalCode(user.getPersonalCode());

        return existingUserOptional.orElseGet(() -> save(user));
    }
}
