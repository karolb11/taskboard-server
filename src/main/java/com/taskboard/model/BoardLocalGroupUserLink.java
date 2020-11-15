package com.taskboard.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
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

    @Column(nullable = false)
    private boolean accepted;

    public BoardLocalGroupUserLink() {
    }

    public BoardLocalGroupUserLink(Board board, LocalRole localRole, User user) {
        this.user = user;
        this.board = board;
        this.localRole = localRole;
    }
    public BoardLocalGroupUserLink(Board board, LocalRole localRole, User user, boolean accepted) {
        this.user = user;
        this.board = board;
        this.localRole = localRole;
        this.accepted = accepted;
    }
}
