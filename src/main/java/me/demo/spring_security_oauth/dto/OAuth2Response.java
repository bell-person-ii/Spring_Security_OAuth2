package me.demo.spring_security_oauth.dto;

public interface OAuth2Response {
    String getProvider(); // 제공자의 이름: 구글 또는 네이버
    String getProviderId(); // 사용자의 아이디
    String getEmail(); // 시용자의 이메일
    String getName(); // 사용자의 이름
}
