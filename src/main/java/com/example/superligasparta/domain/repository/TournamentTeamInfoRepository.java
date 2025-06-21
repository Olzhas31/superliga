package com.example.superligasparta.domain.repository;

import com.example.superligasparta.domain.entity.TournamentTeamInfo;
import com.example.superligasparta.model.TournamentTeamId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTeamInfoRepository extends
    JpaRepository<TournamentTeamInfo, TournamentTeamId> {

  Optional<TournamentTeamInfo> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);

  List<TournamentTeamInfo> findByTournamentId(Long tournamentId);

  boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);

  boolean existsByTournamentIdAndDisplayName(Long tournamentId, String displayName);

  boolean existsByTournamentIdAndDisplayNameIgnoreCaseAndTeamIdNot(Long tournamentId, String displayName, Long excludedTeamId);

}
