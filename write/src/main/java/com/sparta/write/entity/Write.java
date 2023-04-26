package com.sparta.write.entity;


import com.sparta.write.dto.WriteRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Write extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//숫자를 자동으로 더해줌
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

//    @Column(nullable = false)
//    private Long userId;

    public Write(WriteRequestDto requestDto,String username) {

        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = username;

    }

    public void update(WriteRequestDto requestDto) {
        this.title = requestDto.getTitle();
//        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }



}


