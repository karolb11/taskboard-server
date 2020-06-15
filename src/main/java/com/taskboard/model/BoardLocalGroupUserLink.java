package com.taskboard.model;

import javax.persistence.*;

@Entity
public class BoardLocalGroupUserLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "local_role_id", nullable = false)
    private LocalRole localRole;

    public BoardLocalGroupUserLink() {
    }

    public BoardLocalGroupUserLink(Board board, LocalRole localRole, User user) {
        this.user = user;
        this.board = board;
        this.localRole = localRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public LocalRole getLocalRole() {
        return localRole;
    }

    public void setLocalRole(LocalRole localRole) {
        this.localRole = localRole;
    }
}
