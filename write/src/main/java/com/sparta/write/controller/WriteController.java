package com.sparta.write.controller;

import com.sparta.write.dto.ResponseDto;
import com.sparta.write.dto.WriteRequestDto;
import com.sparta.write.dto.WriteResponseDto;
import com.sparta.write.entity.Write;
import com.sparta.write.service.WriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class WriteController {

    private final WriteService writeService;


    @GetMapping("/")//메인 페이지 반환
    public ModelAndView home() {
        return new ModelAndView("index");
    }


    @PostMapping("/api/memos")//등록 부분(?)
    public WriteResponseDto createWrite(@RequestBody WriteRequestDto requestDto, HttpServletRequest request){

        return writeService.createWrite(requestDto, request);
    }



    @GetMapping("/api/memos")//전체 조회//주소가 똑같아도 방식(get,post)달라서 주소 같아도 됨(?)
    public List<WriteResponseDto> getWrites(){
        return writeService.getWrites();
    }


    @GetMapping("/api/memos/{id}")//특정 게시물 조회
    public WriteResponseDto getWrite(@PathVariable Long id) {
        return writeService.getWrite(id);
    }


    @PutMapping("/api/memos/{id}")//수정
    public WriteResponseDto updateWrite(@PathVariable Long id,@RequestBody WriteRequestDto requestDto,HttpServletRequest request){
        return writeService.update(id, requestDto, request);//필요한 값을 넣어줌(id, requestDto)
    }

    @DeleteMapping("/api/memos/{id}")//삭제
    public ResponseDto deleteWrite(@PathVariable Long id, HttpServletRequest request){
//        boolean result = writeService.deleteWrite(id, request);
        writeService.deleteWrite(id, request);
        return new ResponseDto("게시글 삭제 성공", HttpStatus.OK);
    }

}
