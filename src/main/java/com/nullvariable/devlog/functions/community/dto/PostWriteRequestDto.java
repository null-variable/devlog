package com.nullvariable.devlog.functions.community.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostWriteRequestDto {
    private String title;
    private String content;
    private List<String> tags;
}
