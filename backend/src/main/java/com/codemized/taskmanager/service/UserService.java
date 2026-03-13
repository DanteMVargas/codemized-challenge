package com.codemized.taskmanager.service;

import com.codemized.taskmanager.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
}