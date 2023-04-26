package com.sparta.write.controller;

import com.sparta.write.dto.LoginRequestDto;
import com.sparta.write.dto.ResponseDto;
import com.sparta.write.dto.SignupRequestDto;
import com.sparta.write.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor//클래스에 선언되어 있는 final 또는 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성(?)
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")//회원가입 페이지 보여주기(?)
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")//로그인 페이지 보여주기(?)
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }



    @PostMapping("/signup")
    public ResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            StringBuilder st = new StringBuilder();
            for(FieldError fieldError: bindingResult.getFieldErrors()) {
                st.append(fieldError.getDefaultMessage());
            }
            return new ResponseDto(st.toString(), HttpStatus.BAD_REQUEST);
        }
        return userService.signup(signupRequestDto);
    }

//    @PostMapping("/login")
//    public String login(LoginRequestDto loginRequestDto) {
//        userService.login(loginRequestDto);
//        return "redirect:/api/shop";
//    }

    @ResponseBody//(주석처리된 위코드는 이걸로 수정되기전 코드)(ajax형식으로 body값이 넘어오며,HttpServletResponse은 Client쪽으로 객체를 반환하기 위해 이용(?)(Header쪽에 만들어준 Token을 넣어주기 위함(?))
    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
//        ModelAndView modelAndView = new ModelAndView("success");
//        modelAndView.addObject("message", "로그인에 성공하였습니다.");
//        return modelAndView;
    }//jwt 구현하기 8(from ShopController)//이후 UserService로 이동//ResponseHeader에 토큰보내기 파트

}