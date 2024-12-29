package org.example.demomogodb.user;

import lombok.extern.slf4j.Slf4j;
import org.example.demomogodb.exception.UserNotFoundException;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "users", allEntries = true, cacheManager = "redisCacheManager")
    public User saveUser(UserReq req) {
        var user = User.builder()
                .name(req.getName())
                .build();
        userRepository.save(user);
        return user;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "users", allEntries = true, cacheManager = "redisCacheManager")
            },
            put = {
                    @CachePut(value = "user", key = "#req.id", cacheManager = "caffeineCacheManager")
            }
    )
    public User update(UserReq req) {
        User user = userRepository.findById(req.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + req.getId()));

        user.setName(req.getName() != null ? req.getName() : user.getName());
        return userRepository.save(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true, cacheManager = "redisCacheManager"),
            @CacheEvict(value = "user", key = "#idUser", cacheManager = "caffeineCacheManager")
    })
    public void delete(String idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + idUser));
        userRepository.delete(user);
    }

    @Cacheable(value = "users", key = "'listUsers:' + #page + ':' + #size", cacheManager = "redisCacheManager")
    public Page<User> listUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    @Cacheable(value = "users", key = "'listUsers:' + #page + ':' + #size", cacheManager = "redisCacheManager")
    public UserPage listUser(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageRequest);

        UserPage userPageResponse = new UserPage();
        userPageResponse.setContent(userPage.getContent());
        userPageResponse.setTotalElements((int) userPage.getTotalElements());
        userPageResponse.setTotalPages(userPage.getTotalPages());
        userPageResponse.setPageNumber(userPage.getNumber());
        userPageResponse.setPageSize(userPage.getSize());

        return userPageResponse;
    }

    @Cacheable(value = "user", cacheManager = "caffeineCacheManager", key = "#idUser")
    public User getUser(String idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // grpc


}
