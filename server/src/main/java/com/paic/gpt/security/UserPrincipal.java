package com.paic.gpt.security;

import com.paic.gpt.model.User;
import com.paic.gpt.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private static final String defaultRolePrefix = "ROLE_";

    private final Long id;

    private final String name;

    private final String username;

    @JsonIgnore
    private final String email;

    @JsonIgnore
    private final String password;

    private final Integer totalCount;
    private final Integer currCount;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String name, String username,
                         String email, String password,
                         Collection<? extends GrantedAuthority> authorities,
                         int totalCount, int currCount) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.totalCount = totalCount;
        this.currCount = currCount;
    }

    public static UserPrincipal create(User user, List<UserRole> roles, int currCount) {
        List<GrantedAuthority> authorities = roles.stream().map(role ->
                new SimpleGrantedAuthority(defaultRolePrefix + role.getCode())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPwd(),
                authorities,
                user.getMember().getReqCount(),
                currCount
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }


    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getCurrCount() {
        return currCount;
    }
}
