package cn.edu.xmu.freight.controller;

import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.vo.FreightModelVo;
import cn.edu.xmu.freight.service.FreightService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:24
 */
@Api(value = "运费服务",tags ="freight")
@RestController
@RequestMapping(value = "/freight",produces = "application/json;charset=UTF-8")
public class FreightController {


    private static final Logger logger = LoggerFactory.getLogger(FreightController.class);

    @Autowired
    private FreightService freightService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 获取店铺中商品的运费模板
     * @author issyu 30320182200070
     * @date 2020/12/5 1:33
     */

    @ApiOperation(value = "获取店铺中商品的运费模板",produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization", value = "Token", required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "Id", value = "店铺Id", required = true),
            @ApiImplicitParam(paramType = "query",dataType = "String",name = "name", value = "模板名称", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query",dataType = "int",name = "pageSize", value = "每页数目", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0,message = "成功")
    })
    @Audit
    @GetMapping("/shops/{id}/freightmodels")
    public Object getFreightModelsInShop(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "1") Integer pageSize){

        FreightModelVo freightModelVo = new FreightModelVo(id,name);
        ReturnObject<PageInfo<VoObject>> returnObject = freightService.getFreghtModelsInShop(freightModelVo,page,pageSize);
        return Common.getPageRetObject(returnObject);
    }

/**
 * 管理员定义店铺的运费模板
 * @author issyu 30320182200070
 * @date 2020/12/6 11:04
 */
    @ApiOperation(value = "管理员定义店铺的运费模板",produces="application/json")

    public Object defineFreightModel(){

        return null;
    }
}
