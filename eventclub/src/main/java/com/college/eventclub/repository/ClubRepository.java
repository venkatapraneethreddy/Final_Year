package com.college.eventclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.college.eventclub.model.User;
import com.college.eventclub.model.Club;
import com.college.eventclub.model.ClubStatus;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByCreatedBy(User user);
    List<Club> findByStatus(ClubStatus status);

}
