package com.nullvariable.devlog.functions.community;

import com.nullvariable.devlog.functions.community.dto.PostCommentRequestDto;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nullvariable.devlog.functions.community.dto.PostWriteRequestDto;

@RestController
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping("/admin/post")
    public ResponseEntity<Object> postWrite(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostWriteRequestDto dto
    ) throws JoseException, InvalidJwtException, MalformedClaimException {
        return communityService.postWrite(token, dto);
    }

    @GetMapping("/user/post")
    public ResponseEntity<Object> getPost(
            @RequestParam(value = "uuid") String uuid
    ) {
        return communityService.getPost(uuid);
    }

    @PostMapping("/user/comment")
    public ResponseEntity<Object> postComment(
            @RequestBody PostCommentRequestDto dto,
            @RequestHeader(value = "Authorization") String token
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        return communityService.postComment(token, dto);
    }

    @PostMapping("/user/comment/reply")
    public ResponseEntity<Object> replyComment(
            @RequestBody PostCommentRequestDto dto,
            @RequestHeader(value = "Authorization") String token
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        return communityService.replyComment(token, dto);
    }

    @DeleteMapping("/user/comment")
    public ResponseEntity<Object> deleteComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "target") String target
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        return communityService.deleteComment(token, target);
    }
}
