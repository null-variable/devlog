package com.nullvariable.devlog.functions.community;

import com.nullvariable.devlog.entites.AuthEntity;
import com.nullvariable.devlog.entites.CommentEntity;
import com.nullvariable.devlog.entites.PostEntity;
import com.nullvariable.devlog.entites.TagsEntity;
import com.nullvariable.devlog.enums.Roles;
import com.nullvariable.devlog.functions.community.dto.PostCommentRequestDto;
import com.nullvariable.devlog.functions.community.dto.PostWriteRequestDto;
import com.nullvariable.devlog.repositories.AuthRepository;
import com.nullvariable.devlog.repositories.CommentRepository;
import com.nullvariable.devlog.repositories.PostRepository;
import com.nullvariable.devlog.repositories.TagsRepository;
import com.nullvariable.devlog.utils.jsonwebtoken.JwtProvider;
import com.nullvariable.devlog.utils.responses.BasicResponse;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {

    private final AuthRepository authRepository;
    private final PostRepository postRepository;
    private final TagsRepository tagsRepository;
    private final CommentRepository commentRepository;
    private final JwtProvider jwtProvider;

    public CommunityServiceImpl(AuthRepository authRepository, PostRepository postRepository, TagsRepository tagsRepository, CommentRepository commentRepository, JwtProvider jwtProvider) {
        this.authRepository = authRepository;
        this.postRepository = postRepository;
        this.tagsRepository = tagsRepository;
        this.commentRepository = commentRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public ResponseEntity<Object> postWrite(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostWriteRequestDto dto
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        Optional<AuthEntity> user = authRepository.findOneByUUID(jwtProvider.verify(token));

        PostEntity post = PostEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(user)
                .createdAt(LocalDateTime.now())
                .build();

        if (!dto.getTags().isEmpty()) {
            List<TagsEntity> tags = this.addTags(dto.getTags());
            post.setTags(tags);
        }

        postRepository.save(post);

        BasicResponse response = BasicResponse.builder()
                .success(true)
                .message(Optional.of("게시글 작성 완료"))
                .data(Optional.empty())
                .build();


        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<Object> getPost(
            @RequestParam(value = "uuid") String uuid
    ) {
        Optional<PostEntity> post = postRepository.findOneByUUID(uuid);

        if (post.isEmpty()) {
            BasicResponse response = BasicResponse.builder()
                    .success(false)
                    .message(Optional.of("게시물을 찾을 수 없습니다."))
                    .data(Optional.empty())
                    .build();

            return ResponseEntity.status(403).body(response);
        } else {
            BasicResponse response = BasicResponse.builder()
                    .success(true)
                    .message(Optional.of("데이터 불러오기 성공"))
                    .data(Optional.of(post))
                    .build();
            return ResponseEntity.status(200).body(response);
        }
    }

    @Override
    public List<TagsEntity> addTags(List<String> Tags) {
        List<TagsEntity> it = new ArrayList<TagsEntity>();
        for (String tag: Tags) {
            Optional<TagsEntity> object = tagsRepository.findOneByName(tag);
            TagsEntity t = TagsEntity.builder()
                    .name(tag)
                    .createdAt(LocalDateTime.now())
                    .build();
            if (object.isEmpty()) {
                tagsRepository.save(t);
                it.add(t);
            } else {
                it.add(object.get());
            }
        }
        System.out.println(it);
        return it;
    }

    @Override
    public ResponseEntity<Object> postComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostCommentRequestDto dto
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        Optional<AuthEntity> user = authRepository.findOneByUUID(jwtProvider.verify(token));
        Optional<PostEntity> target = postRepository.findOneByUUID(dto.getTarget());

        if (target.isEmpty()) {
            BasicResponse response = BasicResponse.builder()
                    .success(false)
                    .message(Optional.of("게시글을 확인할 수 없습니다."))
                    .data(Optional.empty())
                    .build();

            return ResponseEntity.status(403).body(response);
        } else {
            CommentEntity c = CommentEntity.builder()
                    .author(user)
                    .target(target.get())
                    .content(dto.getContent())
                    .build();

            commentRepository.save(c);

            BasicResponse response = BasicResponse.builder()
                    .success(true)
                    .message(Optional.of("정상적으로 댓글을 추가하였습니다."))
                    .data(Optional.empty())
                    .build();

            return ResponseEntity.status(201).body(response);
        }
    }

    @Override
    public ResponseEntity<Object> replyComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody PostCommentRequestDto dto
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        Optional<AuthEntity> user = authRepository.findOneByUUID(jwtProvider.verify(token));
        Optional<CommentEntity> parent = commentRepository.findOneByUUID(dto.getTarget());

        if (parent.isEmpty()) {
            BasicResponse response = BasicResponse.builder()
                    .success(false)
                    .message(Optional.of("댓글을 확인할 수 없습니다."))
                    .data(Optional.empty())
                    .build();

            return ResponseEntity.status(403).body(response);
        } else {
            CommentEntity c = CommentEntity.builder()
                    .author(user)
                    .content(dto.getContent())
                    .target(parent.get().getTarget())
                    .parent(Optional.of(parent.get()))
                    .build();

            commentRepository.save(c);

            BasicResponse response = BasicResponse.builder()
                    .success(true)
                    .message(Optional.of("정상적으로 댓글을 추가하였습니다."))
                    .data(Optional.empty())
                    .build();

            return ResponseEntity.status(201).body(response);
        }
    }

    @Override
    public ResponseEntity<Object> deleteComment(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam(value = "target") String target
    ) throws InvalidJwtException, MalformedClaimException, JoseException {
        Optional<AuthEntity> user = authRepository.findOneByUUID(jwtProvider.verify(token));
        Optional<CommentEntity> comment = commentRepository.findOneByUUID(target);

        if (comment.isPresent()) {
            if (comment.get().getAuthor().get() == user.get() || user.get().getRole() == Roles.ROLE_ADMIN) {
                commentRepository.delete(comment.get());

                BasicResponse response = BasicResponse.builder()
                        .success(true)
                        .data(Optional.empty())
                        .message(Optional.of("댓글을 삭제했습니다."))
                        .build();

                return ResponseEntity.status(200).body(response);
            } else {
                BasicResponse response = BasicResponse.builder()
                        .success(false)
                        .data(Optional.empty())
                        .message(Optional.of("댓글을 삭제할 권한이 없습니다."))
                        .build();

                return ResponseEntity.status(401).body(response);
            }
        } else {
            BasicResponse response = BasicResponse.builder()
                    .success(false)
                    .data(Optional.empty())
                    .message(Optional.of("댓글을 확인할 수 없습니다."))
                    .build();

            return ResponseEntity.status(403).body(response);
        }
    }
}
