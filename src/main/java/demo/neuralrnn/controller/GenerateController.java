package demo.neuralrnn.controller;

import demo.neuralrnn.config.DataPrepareInterceptor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class GenerateController {
    private static Logger logger = Logger.getLogger(GenerateController.class);

    @RequestMapping("/generate")
    public String generate(HttpSession session) {
        session.getServletContext().setAttribute(DataPrepareInterceptor.DATA_KEY, null);
        return "forward:/";
    }

}
