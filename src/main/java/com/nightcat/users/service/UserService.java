package com.nightcat.users.service;

import com.nightcat.common.utility.Assert;
import com.nightcat.common.utility.Util;
import com.nightcat.entity.User;
import com.nightcat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.nightcat.common.constant.HttpStatus.*;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;


    public void save(User user) {
        User olduser = userRepository.findByPhone(user.getPhone());
        Assert.isNull(olduser, BAD_REQUEST, "手机号已被使用");

        user.setUid(Util.uuid());
        user.setImtoken(user.getUid());
        userRepository.save(user);
    }


    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public void saveOrUpdate(User user) {
        userRepository.saveOrUpdate(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public List<User> sort(List<User> T) {
        return userRepository.sort(T);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findBy(String[] keys, String[] values) {
        return userRepository.findBy(keys, values);
    }

    public List<User> findBy(String key, String value) {
        return userRepository.findBy(key, value);
    }

    public List<User> findBy(String key, String value, boolean isLikeQuery) {
        return userRepository.findBy(key, value, isLikeQuery);
    }

    public List<User> findBy(String[] keys, String[] values, boolean isLikeQuery) {
        return userRepository.findBy(keys, values, isLikeQuery);
    }

    public User findById(String id) {
        User user = userRepository.findById(id);
        user.setPassword(null);
        return user;
    }

    public User findByIds(Map<String, String> idAndValues) {
        return userRepository.findByIds(idAndValues);
    }

    public List<User> findBy(Map<String, String> attr, boolean likeQuery) {
        return userRepository.findBy(attr, likeQuery);
    }

}
