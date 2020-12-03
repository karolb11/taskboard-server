package com.taskboard.repository;

import com.taskboard.model.Board;
import com.taskboard.payload.BoardViewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<BoardViewResponse> findBoardViewByIdIn(Iterable<Long> ids);
    List<BoardViewResponse> findBoardViewByIdInAndArchivedIsFalse(Iterable<Long> ids);
    Optional<Board> findBoardById(Long id);

}
