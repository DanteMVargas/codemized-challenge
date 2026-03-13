package com.codemized.taskmanager.service.impl;

import com.codemized.taskmanager.dto.response.UserResponse;
import com.codemized.taskmanager.repository.UserRepository;
import com.codemized.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream().map(UserResponse::from).toList();
    }
}