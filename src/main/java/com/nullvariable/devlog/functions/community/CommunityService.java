package com.nullvariable.devlog.functions.community;

import com.nullvariable.devlog.entites.TagsEntity;
import com.nullvariable.devlog.functions.community.dto.PostCommentRequestDto;
import com.nullvariable.devlog.functions.community.dto.PostWriteRequestDto;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public interface CommunityService {
    ResponseEntity<Object> postWrite(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostWriteRequestDto dto
    ) throws JoseException, InvalidJwtException, MalformedClaimException;

    ResponseEntity<Object> getPost(
            @RequestParam(value = "uuid") String uuid
    );

    List<TagsEntity> addTags(
            List<String> tags
    );

    ResponseEntity<Object> postComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostCommentRequestDto dto
    ) throws InvalidJwtException, MalformedClaimException, JoseException;

    ResponseEntity<Object> replyComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostCommentRequestDto dto
    ) throws InvalidJwtException, MalformedClaimException, JoseException;

    ResponseEntity<Object> deleteComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "target") String target
    ) throws InvalidJwtException, MalformedClaimException, JoseException;
}
