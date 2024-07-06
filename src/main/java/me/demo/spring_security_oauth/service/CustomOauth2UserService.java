package me.demo.spring_security_oauth.service;

import lombok.RequiredArgsConstructor;
import me.demo.spring_security_oauth.domain.UserEntity;
import me.demo.spring_security_oauth.dto.CustomOAuth2User;
import me.demo.spring_security_oauth.dto.GoogleResponse;
import me.demo.spring_security_oauth.dto.NaverResonse;
import me.demo.spring_security_oauth.dto.OAuth2Response;
import me.demo.spring_security_oauth.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

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

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);
        String role = null;

        if(existData == null){ // 기존에 존재하지 않는 회원인 경우
            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(userEntity);
        }
        else{
            role = existData.getRole();
            existData.updateEmail(oAuth2Response.getEmail());
        }
        return new CustomOAuth2User(oAuth2Response, role);
    }
}
