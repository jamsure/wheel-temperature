package chaoyang.tempmonitor.controller;

import chaoyang.tempmonitor.model.User;
import chaoyang.tempmonitor.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@ComponentScan({"chaoyang.tempmonitor.service"})
@MapperScan("chaoyang.tempmonitor.mapper")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/find")
    public String find(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.find(1);
        ModelAndView mav = new ModelAndView();
        return "Hello World"+ username+ password;
    }

    @RequestMapping("/login")
    public String print(){
        return "hahhaha";
    }


}
