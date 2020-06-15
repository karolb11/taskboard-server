package com.taskboard.repository;

import com.taskboard.model.Board;
import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.LocalRole;
import com.taskboard.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLocalGroupUserLinkRepository extends CrudRepository<BoardLocalGroupUserLink, Long> {
    Iterable<BoardLocalGroupUserLink> findByUserAndBoard(User user, Board board);
    Iterable<BoardLocalGroupUserLink> findByUser(User user);
}
