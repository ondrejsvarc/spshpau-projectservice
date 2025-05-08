package com.spshpau.projectservice.services.impl;

import com.spshpau.projectservice.dto.UserSummaryDto;
import com.spshpau.projectservice.services.exceptions.UserNotFoundException;
import com.spshpau.projectservice.model.SimpleUser;
import com.spshpau.projectservice.repositories.SimpleUserRepository;
import com.spshpau.projectservice.services.SimpleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimpleUserServiceImpl implements SimpleUserService {
    private final SimpleUserRepository simpleUserRepository;

    @Override
    public SimpleUser findUserById(UUID userId) {
        return simpleUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    @Override
    @Transactional
    public SimpleUser getOrCreateSimpleUser(UserSummaryDto userSummaryDto) {
        if (userSummaryDto == null || userSummaryDto.getId() == null) {
            throw new IllegalArgumentException("UserSummaryDto or its ID cannot be null");
        }
        return simpleUserRepository.findById(userSummaryDto.getId())
                .orElseGet(() -> {
                    SimpleUser newUser = new SimpleUser();
                    newUser.setId(userSummaryDto.getId());
                    newUser.setUsername(userSummaryDto.getUsername());
                    newUser.setFirstName(userSummaryDto.getFirstName());
                    newUser.setLastName(userSummaryDto.getLastName());
                    newUser.setLocation(userSummaryDto.getLocation());
                    return simpleUserRepository.save(newUser);
                });
    }

    @Override
    @Transactional
    public SimpleUser getOrCreateSimpleUser(UUID userId, String username, String firstName, String lastName, String location) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return simpleUserRepository.findById(userId)
                .orElseGet(() -> {
                    SimpleUser newUser = new SimpleUser();
                    newUser.setId(userId);
                    newUser.setUsername(username);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setLocation(location);
                    return simpleUserRepository.save(newUser);
                });
    }
}
