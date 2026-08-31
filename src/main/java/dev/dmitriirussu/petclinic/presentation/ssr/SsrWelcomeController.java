package dev.dmitriirussu.petclinic.presentation.ssr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SsrWelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
}
