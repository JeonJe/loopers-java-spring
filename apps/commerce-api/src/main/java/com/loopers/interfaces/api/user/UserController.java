package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserResult;
import com.loopers.interfaces.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApiSpec {

    private final UserFacade userFacade;

    @PostMapping("/register")
    @Override
    public ApiResponse<UserDto.UserResponse> register(
        @Valid @RequestBody UserDto.RegisterRequest request
    ) {
        UserResult userResult = userFacade.register(
            request.userId(),
            request.email(),
            request.birth(),
            request.gender()
        );

        UserDto.UserResponse response = UserDto.UserResponse.from(userResult);
        return ApiResponse.success(response);
    }

    @GetMapping("/{userId}")
    @Override
    public ApiResponse<UserDto.UserResponse> getUser(@PathVariable String userId) {
        UserResult userResult = userFacade.getUser(userId);
        UserDto.UserResponse response = UserDto.UserResponse.from(userResult);
        return ApiResponse.success(response);
    }
}
