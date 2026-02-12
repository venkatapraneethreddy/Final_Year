package com.college.eventclub.service;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Club;
import com.college.eventclub.model.ClubStatus;
import com.college.eventclub.repository.ClubRepository;
import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public Club createClub(Club club) {
        club.setStatus(ClubStatus.PENDING);
        return clubRepository.save(club);
    }

    public Club approveClub(Long clubId) {

    Club club = clubRepository.findById(clubId)
            .orElseThrow(() -> new RuntimeException("Club not found"));

    club.setStatus(ClubStatus.APPROVED);
    return clubRepository.save(club);
    }

    public List<Club> getApprovedClubs() {
    return clubRepository.findAll()
            .stream()
            .filter(club -> club.getStatus() == ClubStatus.APPROVED)
            .toList();
}

}
