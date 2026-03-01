package com.college.eventclub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.college.eventclub.model.Club;
import com.college.eventclub.model.ClubStatus;
import com.college.eventclub.repository.ClubRepository;

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

    // NEW: Reject club
    public Club rejectClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));
        club.setStatus(ClubStatus.REJECTED);
        return clubRepository.save(club);
    }

    public List<Club> getApprovedClubs() {
        return clubRepository.findByStatus(ClubStatus.APPROVED);
    }

    public List<Club> getPendingClubs() {
        return clubRepository.findByStatus(ClubStatus.PENDING);
    }

    public Optional<Club> getMyClub(String email) {
        return clubRepository.findByCreatedByEmail(email);
    }
}
