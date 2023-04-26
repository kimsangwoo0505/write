package com.sparta.write.dto;


import com.sparta.write.entity.Write;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriteResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String username;

    public WriteResponseDto(Write write) {
        this.id = write.getId();
        this.title = write.getTitle();
        this.contents = write.getContents();
        this.username = write.getUsername();
    }
}