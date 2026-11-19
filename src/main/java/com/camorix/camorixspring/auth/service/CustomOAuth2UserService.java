package com.camorix.camorixspring.auth.service;

import com.camorix.camorixspring.auth.dto.*;
import com.camorix.camorixspring.auth.dto.oauth2.*;
import com.camorix.camorixspring.auth.entity.BaseUser;
import com.camorix.camorixspring.auth.entity.NokUser;
import com.camorix.camorixspring.auth.entity.NurseUser;
import com.camorix.camorixspring.auth.repository.NokUserRepository;
import com.camorix.camorixspring.auth.repository.NurseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final NokUserRepository nokUserRepository;
    private final NurseUserRepository nurseUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        UserDTO userDTO = new UserDTO();
        BaseUser userEntity = null;

//        네이버 로그인의 경우 NaverResponse
//        구글 로그인의 경우 GoogleResponse
//        카카오 로그인의 경우 KakaoResponse
        if (registrationId.startsWith("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.startsWith("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.startsWith("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        userDTO.setEmail(oAuth2Response.getEmail());

//        naver-user, google-user로 로그인시 일반 사용자 (User)
//        naver-nurse, google-nurse로 로그인시 간호사 (Nurse)
        if (registrationId.endsWith("user")) {
            userDTO.setRole(UserRole.USER);
            userEntity = nokUserRepository.findByEmail(oAuth2Response.getEmail());
            if (userEntity == null) {
                userEntity = NokUser.builder()
                        .email(oAuth2Response.getEmail())
                        .name(oAuth2Response.getName())
                        .build();
                nokUserRepository.save((NokUser) userEntity);
            }
        } else if (registrationId.endsWith("nurse")) {
            userDTO.setRole(UserRole.NURSE);
            userEntity = nurseUserRepository.findByEmail(oAuth2Response.getEmail());
            if (userEntity == null) {
                userEntity = NurseUser.builder()
                        .email(oAuth2Response.getEmail())
                        .name(oAuth2Response.getName())
                        .build();
                nurseUserRepository.save((NurseUser) userEntity);
            }
        } else {
            return null;
        }

        userDTO.setName(userEntity==null? oAuth2User.getName() : userEntity.getName());

        return new CustomOAuth2User(userDTO);
    }
}
