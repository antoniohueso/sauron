package com.corpme.sauron.service;

import com.corpme.sauron.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ahg on 22/12/14.
 */
@Service
public class UsersService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RfcRepository rfcRepository;

    Logger logger = LoggerFactory.getLogger(getClass());

    public Iterable<User> usuariosServiciosCentrales() {
        return userRepository.findAllFromServiciosCentrales();
    }

    public Iterable<User> usuariosDisponibles() {
        return userRepository.findDisponiblesFromServiciosCentrales();
    }


    public User user(Long id) {
        return userRepository.findOne(id);
    }

}
