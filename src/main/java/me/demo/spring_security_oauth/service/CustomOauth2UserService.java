package me.demo.spring_security_oauth.service;

import me.demo.spring_security_oauth.dto.CustomOAuth2User;
import me.demo.spring_security_oauth.dto.GoogleResponse;
import me.demo.spring_security_oauth.dto.NaverResonse;
import me.demo.spring_security_oauth.dto.OAuth2Response;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("naver")){ //인증 제공자가 네이버 인 경우
            oAuth2Response = new NaverResonse(oAuth2User.getAttributes());
        }

        else if(registrationId.equals("google")){ // 인증 제공자가 구글인 경우
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }

        else {
            return null;
        }

        String role = "ROLE_USER";

        return new CustomOAuth2User(oAuth2Response, role);
    }
}
