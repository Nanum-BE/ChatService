package com.nanum.webfluxservice.alert.vo;

import com.nanum.webfluxservice.alert.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class AlertRequest {
    @NotNull(message = "content cannot be null")
    @Size(min = 1)
    @Schema(description = "해당 내용 입력하세요.",defaultValue = "쪽지 생성되었습니다.")
    private String content;

    @NotNull(message = "title cannot be null")
    @Size(min = 1)
    @Schema(description = "어떤 기능인지 입력하세요.쪽지, 커뮤니티,,,",
            defaultValue = "쪽지")
    private String title;

    @NotNull(message = "userIds cannot be null")
    @Size(min = 1)
    @Schema(description = "해당 유저의 ID값들을 입력하세요.",defaultValue = "[1, 2, 3]")
    private List<Long> userIds;

    @NotNull(message = "url cannot be null")
    @Size(min = 1)
    @Schema(description = "링크를 입력하세요.",defaultValue = "http://localhost:")
    private String url;

}

