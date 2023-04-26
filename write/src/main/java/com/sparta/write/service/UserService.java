package com.sparta.write.service;

import com.sparta.write.dto.LoginRequestDto;
import com.sparta.write.dto.ResponseDto;
import com.sparta.write.dto.SignupRequestDto;
import com.sparta.write.entity.User;
import com.sparta.write.entity.UserRoleEnum;
import com.sparta.write.jwt.JwtUtil;
import com.sparta.write.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;//jwt 구현하기 10(from UserService)//이후 User로 이동//ResponseHeader에 토큰보내기 파트(11번부터는 jwt로 관심상품 조회하기 파트)//+로 login.html에 script가 추가됨(추가된 파트는 //토큰보내기로 표시)+로 index.html과 basic.js도 스니펫붙임(최종본반영해도 좋을수 있음))


    @Transactional
    public ResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }


        User user = new User(username, password);
        userRepository.save(user);

        return new ResponseDto("회원가입 성공", HttpStatus.OK);
    }

//    @Transactional(readOnly = true)
//    public void login(LoginRequestDto loginRequestDto) {
//        String username = loginRequestDto.getUsername();
//        String password = loginRequestDto.getPassword();
//
//        // 사용자 확인
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
//        );
//
//        // 비밀번호 확인
//        if(!user.getPassword().equals(password)){
//            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//    }

    @Transactional(readOnly = true)
    public ResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));//addHeader를 사용해 Header쪽에 값을 넣어줄 수 있음(?)(유저이름과 유저 권한을 넣어줌(?))
        return new ResponseDto("로그인 성공", HttpStatus.OK);

    }//jwt 구현하기 9(from UserController)//이후 의존성 주입을 위해 동일 부분에서 작업(UserService)//ResponseHeader에 토큰보내기 파트

}