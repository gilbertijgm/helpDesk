package com.helpdesk.helpDesk.controller.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String username,
                                    @NotBlank String password,
                                    @Email @NotBlank String email,
                                    @Valid AuthCreateRoleRequest roleRequest) {
}
