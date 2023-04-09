package com.paic.gpt.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String prePassword;

    @NotBlank
    private String newPassword;

}
