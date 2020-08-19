package kr.co.lunasoft.batchadmin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin(origins="*")
@Controller
@Slf4j
public class BatchAdminController implements ErrorController {

    @GetMapping("/error")
    public String redirectRoot() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return "/error";
    }
}
