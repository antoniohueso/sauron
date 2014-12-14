package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ahg on 14/12/14.
 */
@Service
public class UsersService {

    @Autowired
    UserRepository userRepository;

    public Iterable<User> assignableUsers() {
        return userRepository.findByRolIn(RolType.Desarrollador.getValue(),RolType.Tester.getValue());
    }

}
