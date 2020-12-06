package cn.edu.xmu.freight.service;

import cn.edu.xmu.freight.dao.FreightDao;
import cn.edu.xmu.freight.model.bo.FreightModelBo;
import cn.edu.xmu.freight.model.vo.FreightModelVo;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:24
 */
@Service
public class FreightService {

    @Autowired
    private FreightDao freightDao;

    public ReturnObject getFreghtModelsInShop(FreightModelVo freightModelVo, Integer page, Integer pageSize) {

        ReturnObject returnObject = freightDao.getFreghtModelsInShop(freightModelVo,page,pageSize);
        return returnObject;
    }
}
