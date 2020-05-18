package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attacker")
public class Attacker {

    @GetMapping("/csrf")
    public String csrfAttack(){
        return "attacker/attacker";
    }

}
