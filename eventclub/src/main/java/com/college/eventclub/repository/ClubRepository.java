package com.college.eventclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.college.eventclub.model.User;
import com.college.eventclub.model.Club;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByCreatedBy(User user);

}
