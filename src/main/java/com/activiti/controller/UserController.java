package com.activiti.controller;

import com.activiti.entity.User;
import com.activiti.entity.UserInfo;
import com.activiti.general.DataGrid;
import com.activiti.general.ResponseData;
import com.activiti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author ch.zhang
 * @Date 2019/6/5 19:26
 **/
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/userinfo")
    public ModelAndView userinfo(){return new ModelAndView("userinfo");}
    @GetMapping(value = "/toLogin")
    public ModelAndView main(){
        return new ModelAndView("login");
    }
    @GetMapping(value = "/index")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    /**
     * Description 登录验证
     * @Param [user, request]
     * @Return com.activiti.general.ResponseData
     * @Author zhangcheng
     * @Date 2019/6/11 20:31
     */
    @PostMapping(value="/login")
    public ResponseData login(@RequestBody User user,HttpServletRequest request){
        User oldUser = userService.login(user);
        if(oldUser == null){
            return ResponseData.error(5000,"账号不存在");
        }
        String password = oldUser.getPassword();
        if(!password.equalsIgnoreCase(user.getPassword())){
           return ResponseData.error(5000,"密码错误");
        }
        HttpSession session = request.getSession();
        session.setAttribute("user",oldUser);
        return ResponseData.success();
    }
    @GetMapping(value="/loginout")
    public ModelAndView loginout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        request.getSession().invalidate();
        return new ModelAndView("login");
    }
    @PostMapping(value="/getUser")
    public User getUser(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        return user;
    }
    @PostMapping(value ="/getUserInfoList")
    public DataGrid<UserInfo> getUserInfoList(@RequestParam("current") int current,@RequestParam("rowCount") int rowCount){
        List<UserInfo> userInfoList = userService.getUserInfoList();
        DataGrid<UserInfo> grid=new DataGrid<>();
        grid.setRowCount(rowCount);
        grid.setCurrent(current);
        grid.setTotal(userInfoList.size());
        grid.setRows(userInfoList);
        return grid;
    }


}
