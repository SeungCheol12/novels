package com.example.novels.member.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.novels.member.entity.constant.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class MemberDTO extends User /* implements OAuth2User */ {
    // member entity 정보 + 인증정보 => extends User

    private String email;

    private String pw;

    private String name;
    private String nickname;

    private boolean fromSocial;
    private List<String> roles = new ArrayList<>();

    // OAuth2User 가 넘겨주는 attr 담기 위해
    private Map<String, Object> attr;

    public MemberDTO(String username, String pw, String nickname, boolean fromSocial,
            List<String> roles) {
        super(username, pw,
                roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList()));
        this.email = username;
        this.pw = pw;
        this.fromSocial = fromSocial;
        this.nickname = nickname;
        this.roles = roles;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> datMap = new HashMap<>();

        datMap.put("email", email);
        datMap.put("pw", pw);
        datMap.put("nickname", nickname);
        datMap.put("social", fromSocial);
        datMap.put("roles", roles);
        return datMap;
    }
    // public MemberDTO(String username, String pw, String nickname, boolean
    // fromSocial,
    // Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr)
    // {
    // this(username, pw, nickname, fromSocial, authorities);

    // this.attr = attr;
    // }
    // OAuth2User
    // @Override
    // public Map<String, Object> getAttributes() {

    // return this.attr;
    // }

}
