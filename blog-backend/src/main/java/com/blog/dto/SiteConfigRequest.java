package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SiteConfigRequest {
    @NotBlank(message = "键不能为空")
    private String configKey;
    private String configValue;
}
