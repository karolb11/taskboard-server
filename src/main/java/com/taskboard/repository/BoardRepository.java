package com.taskboard.repository;

import com.taskboard.model.Board;
import com.taskboard.payload.BoardView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<BoardView> findBoardViewByIdIn(Iterable<Long> ids);
    Optional<BoardView> findBoardViewById(Long id);
    Optional<Board> findBoardById(Long id);

}
