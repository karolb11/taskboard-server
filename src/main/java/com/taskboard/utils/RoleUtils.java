package com.taskboard.utils;

import com.taskboard.model.BoardLocalGroupUserLink;
import com.taskboard.model.User;
import com.taskboard.repository.BoardLocalGroupUserLinkRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RoleUtils {
    private static BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository;

    public RoleUtils(BoardLocalGroupUserLinkRepository boardLocalGroupUserLinkRepository) {
        RoleUtils.boardLocalGroupUserLinkRepository = boardLocalGroupUserLinkRepository;
    }

    public static List<GrantedAuthority> UserAuthoritiesAsList(User user) {
        List<GrantedAuthority> globalAuthorities = getUserGlobalAuthorities(user);
        List<GrantedAuthority> localAuthorities = getUsersLocalAuthorities(user);
        return Stream.of(globalAuthorities, localAuthorities)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static List<GrantedAuthority> getUserGlobalAuthorities(User user) {
        return user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());
    }

    private static List<GrantedAuthority> getUsersLocalAuthorities(User user) {
        List<BoardLocalGroupUserLink> roles =
                boardLocalGroupUserLinkRepository.findByUser(user);
        return roles.stream()
                .filter(BoardLocalGroupUserLink::isAccepted)
                .map(role ->
                new SimpleGrantedAuthority("board" + role.getBoard().getId() +
                        ":" + role.getLocalRole().getName().toString())
        ).collect(Collectors.toList());
    }
}
