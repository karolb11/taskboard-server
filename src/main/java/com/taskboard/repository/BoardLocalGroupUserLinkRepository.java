package com.taskboard.repository;

import com.taskboard.model.Board;
import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.LocalRole;
import com.taskboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BoardLocalGroupUserLinkRepository extends JpaRepository<BoardLocalGroupUserLink, Long> {
    Optional<BoardLocalGroupUserLink> findFirstByUserIdAndBoardIdOrderByLocalRoleDesc(Long userId, Long boardId);
    List<BoardLocalGroupUserLink> findByUser(User user);
    List<BoardLocalGroupUserLink> findByUserAndAcceptedIsTrue(User user);
    Set<BoardLocalGroupUserLink> findByBoardAndLocalRoleGreaterThanEqual(Board board, LocalRole localRole);
    Set<BoardLocalGroupUserLink> findByBoardIdAndLocalRoleGreaterThanEqualAndAcceptedIsTrue(Long board_id, LocalRole localRole);
    Iterable<BoardLocalGroupUserLink> findByUserAndBoard(User user, Board board);
    List<BoardLocalGroupUserLink> findByBoardId(Long boardId);
    List<BoardLocalGroupUserLink> findAllByUserAndAcceptedIsFalse(User user);
    Set<BoardLocalGroupUserLink> findByBoardIdAndLocalRoleGreaterThanEqual(Long boardId, LocalRole role);
}
