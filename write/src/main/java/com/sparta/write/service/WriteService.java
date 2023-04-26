package com.sparta.write.service;


import com.sparta.write.dto.ResponseDto;
import com.sparta.write.dto.WriteRequestDto;
import com.sparta.write.dto.WriteResponseDto;
import com.sparta.write.entity.User;
import com.sparta.write.entity.Write;
import com.sparta.write.jwt.JwtUtil;
import com.sparta.write.repository.UserRepository;
import com.sparta.write.repository.WriteRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WriteService {

    private final WriteRepository writeRepository;
    private final UserRepository userRepository;//jwt 구현하기 16(from ProductService)//이후 ProductController로 이동
    private final JwtUtil jwtUtil;

//    @Transactional
//    public Write createWrite(WriteRequestDto requestDto) {
//        Write write = new Write(requestDto);
//        writeRepository.save(write);
//
//        return write;
//    }

    @Transactional//등록 부분(?)
    public WriteResponseDto createWrite(WriteRequestDto requestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 관심상품 추가 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            Write write = writeRepository.save(new Write(requestDto,user.getUsername()));
//save와 달리 saveAndFlush는 영속성 컨테스트를 거치지않고 바로 db에 꽃아 놓음

            return new WriteResponseDto(write);

        } else {
            return null;
        }

    }

    @Transactional(readOnly = true)//전체조회(?)
    public List<WriteResponseDto> getWrites() {
        List<Write> writeList= writeRepository.findAll();
        List<WriteResponseDto>WriteResponseDto= new ArrayList<>();

        for (Write write : writeList) {
            WriteResponseDto responseDto = new WriteResponseDto(write);
            WriteResponseDto.add(responseDto);
        }

        return WriteResponseDto;
    }


    @Transactional(readOnly = true)//부분조회(?)
    public WriteResponseDto getWrite(Long id) {
        Write write = writeRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("회원 상세 조회 실패"));
        return new WriteResponseDto(write);
    }


        @Transactional//수정 부분(?)
        public WriteResponseDto update(Long id, WriteRequestDto requestDto, HttpServletRequest request) {
            // Request에서 Token 가져오기
            String token = jwtUtil.resolveToken(request);
            Claims claims;

            // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
            if (token != null) {
                // Token 검증
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보 가져오기
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("Token Error");
                }

                // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                Write write = writeRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                        () -> new NullPointerException("해당 게시글은 존재하지 않습니다.")
                );


                write.update(requestDto);

                return new WriteResponseDto(write);

            } else {
                return null;
            }
        }//jwt 구현하기 20(from ProductController)


    @Transactional//삭제 부분(?)
    public ResponseDto deleteWrite(Long id, HttpServletRequest request) {
            // Request에서 Token 가져오기
            String token = jwtUtil.resolveToken(request);
            Claims claims;

            // 토큰이 있는 경우에만 관심상품 최저가 업데이트 가능
            if (token != null) {
                // Token 검증
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보 가져오기
                    claims = jwtUtil.getUserInfoFromToken(token);
                } else {
                    throw new IllegalArgumentException("Token Error");
                }

                // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                Write write = writeRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
                        () -> new NullPointerException("해당 게시글은 존재하지 않습니다.")
                );


                writeRepository.delete(write);

                return new ResponseDto("게시글 삭제 성공", HttpStatus.OK);

            } else {
                return new ResponseDto("게시글 삭제 실패", HttpStatus.BAD_REQUEST);
            }
    }

}
