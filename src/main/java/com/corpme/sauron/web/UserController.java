package com.corpme.sauron.web;

import com.corpme.sauron.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ahg on 14/12/14.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UsersService usersService;

    @RequestMapping(method = RequestMethod.GET, value = "/disponibilidad")
    public String disponibles(Model model) {

        model.addAttribute("users",usersService.usuariosDisponibles());

        return "disponibilidad";
    }
}
