package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.repositories.SimpleUserRepository;
import com.spshpau.projectservice.services.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleUserServiceImpl implements SimpleUserService {
    private final SimpleUserRepository simpleUserRepository;
}
