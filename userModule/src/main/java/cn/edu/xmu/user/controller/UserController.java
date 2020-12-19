package cn.edu.xmu.user.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.vo.*;
import cn.edu.xmu.user.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "买家API", tags = "买家API")
@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    private  static  final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * @author 张晨远
     * @return 用户状态列表
     */
    @ApiOperation(value = "获得所有买家的状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/users/states")
    public Object getAllUserState(){
        logger.debug("start: getAllUserState");
        Customer.State[] states=Customer.State.class.getEnumConstants();
        List<CustomerStateRetVo> stateVos= new ArrayList<>();
        for (Customer.State state : states) {
            stateVos.add(new CustomerStateRetVo(state));
        }
        return ResponseUtil.ok(new ReturnObject<>(stateVos).getData());
    }

    /**
     * @author 张晨远
     * @return 注册的用户信息
     */
    @ApiOperation(value = "注册用户", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerCreateVo", name = "vo", value = "注册用户的信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 731,message = "用户名已被注册"),
            @ApiResponse(code = 732,message = "邮箱已被注册"),
            @ApiResponse(code = 733,message = "电话已被注册"),
    })
    //无须验证
    @PostMapping("/users")
    public Object registerUser(@Validated @RequestBody CustomerCreateVo customerCreateVo, BindingResult result){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        logger.debug("start: registerUser");
        ReturnObject<Object> returnObject=userService.registerUser(customerCreateVo);
        if(returnObject.getCode()== ResponseCode.OK){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok(returnObject.getData());
        }
        else return ResponseUtil.fail(returnObject.getCode());
    }

    /**
     * @author 张晨远
     * @param id 用户id
     * @return 用户信息
     */
    @ApiOperation(value = "买家查看自己信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/users")
    public Object getSelfInfo(@LoginUser Long id){
        logger.debug("start: getSelfInfo");
        ReturnObject<Object> returnObject=userService.getUserInfo(id);
        if(returnObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param id 用户id
     */
    @ApiOperation(value = "买家修改自己信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "CustomerModifyVo", name = "vo", value = "用户修改的信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/users")
    public Object modifySelfInfo(@LoginUser Long id, @Validated @RequestBody CustomerModifyVo customerModifyVo, BindingResult result){
        logger.debug("modifySelfInfo: userID = "+id);
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        ReturnObject<Object> returnObject= userService.modifySelfInfo(id,customerModifyVo);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param customerPasswordVo 密码信息
     */
    @ApiOperation(value = "用户修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerPasswordVo", name = "vo", value = "密码信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或密码错误"),
            @ApiResponse(code = 741, message = "新密码不能与旧密码相同"),
    })
    //无须验证
    @PutMapping("/users/password")
    public Object modifyPassword(@Validated @RequestBody CustomerPasswordVo customerPasswordVo, BindingResult result){
        logger.debug("start: modifyPassword");
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        ReturnObject<Object> returnObject= userService.modifyPassword(customerPasswordVo);

        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param customerResetVo 用户名和邮箱信息
     */
    @ApiOperation(value = "用户重置密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerResetVo", name = "vo", value = "用户名和邮箱信息", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
            @ApiResponse(code = 746, message = "与系统预留的电话不一致"),
    })
    //无须验证
    @PutMapping("/users/password/reset")
    public Object resetPassword(@Validated @RequestBody CustomerResetVo customerResetVo, BindingResult result){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        logger.debug("start: resetPassword");
        ReturnObject<Object> returnObject= userService.resetPassword(customerResetVo);

        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param userName 用户名
     * @param email 邮箱
     * @param mobile 电话号码
     * @param page 页码
     * @param pageSize 每页数目
     * @return 用户信息列表
     */
    @ApiOperation(value = "平台管理员获得所有用户列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "email", value = "邮箱", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "mobile", value = "电话号码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页数目", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/users/all")
    public Object getAllUsers(@RequestParam(required = false) String userName, @RequestParam(required = false)String email, @RequestParam(required = false)String mobile, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize){
        page = (page == null) ? 1 : page;
        pageSize = (pageSize == null) ? 10 : pageSize;
        logger.debug("getAllUsers: page = "+ page +"  pageSize =" + pageSize);

        ReturnObject<PageInfo<VoObject>> returnObject = userService.queryUsers(userName, email, mobile, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param customerLoginVo 用户名和密码
     */
    @ApiOperation(value = "用户名密码登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CustomerLoginVo", name = "customerLoginVo", value = "用户名和密码", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或密码错误"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
    })
    //无须验证
    @PostMapping("/users/login")
    public Object login(@Validated @RequestBody CustomerLoginVo customerLoginVo, BindingResult result){
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        logger.debug("start: login");
        ReturnObject<String> returnObject= userService.login(customerLoginVo.getUserName(), customerLoginVo.getPassword());
        if(returnObject.getCode()==ResponseCode.OK){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        }
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     */
    @ApiOperation(value = "用户登出", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/users/logout")
    public Object logout(@RequestHeader("Authorization") String token, @LoginUser Long id){
        logger.debug("start: logout");
        ReturnObject<Object> returnObject=userService.logout(token, id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param id 用户id
     */
    @ApiOperation(value = "管理员查看任意买家信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "customerId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("/users/{id}")
    public Object getUserById(@PathVariable Long id){
        logger.debug("start: getUserById");
        ReturnObject<Object> returnObject=userService.getUserInfo(id);

        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param id 用户id
     */
    @ApiOperation(value = "平台管理员封禁买家", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "customerId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{did}/users/{id}/ban")
    public Object banUser(@PathVariable Long id){
        logger.debug("start: banUser");
        ReturnObject<Object> returnObject=userService.banUser(id);

        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author 张晨远
     * @param id 用户id
     */
    @ApiOperation(value = "平台管理员解禁买家", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "customerId", value = "用户ID", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("/shops/{did}/users/{id}/release")
    public Object unbanUser(@PathVariable Long id){
        logger.debug("start: unbanUser");
        ReturnObject<Object> returnObject=userService.unbanUser(id);

        return Common.decorateReturnObject(returnObject);
    }
}
