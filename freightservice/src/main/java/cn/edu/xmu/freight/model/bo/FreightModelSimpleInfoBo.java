package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.freight.model.vo.FreightModelSimpleInfoRetVo;
import lombok.Data;

import java.time.LocalDateTime;

/*
* @author 史韬韬
* created in 2020/12/8
 */
@Data
public class FreightModelSimpleInfoBo {
    private Long id;
    private String name;
    private Byte type;
    private Boolean isDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public FreightModelSimpleInfoBo(FreightModelPo freightModelPo){
        this.id=freightModelPo.getId();
        this.name=freightModelPo.getName();
        this.type=freightModelPo.getType();
        //不知道这个isDefault是什么含义
        this.isDefault=freightModelPo.getDefaultModel().equals((byte)0);
        this.gmtCreate=freightModelPo.getGmtCreate();
        this.gmtModified=freightModelPo.getGmtModified();
    }

    public FreightModelSimpleInfoRetVo createfreightModelSimpleInfoRetVo(){
        FreightModelSimpleInfoRetVo freightModelSimpleInfoRetVo=new FreightModelSimpleInfoRetVo();
        freightModelSimpleInfoRetVo.setId(id);
        freightModelSimpleInfoRetVo.setGmtCreate(this.getGmtCreate());
        freightModelSimpleInfoRetVo.setGmtModified(this.getGmtModified());
        //还是这个default属性具体什么含义的问题
        freightModelSimpleInfoRetVo.setIsDefault(this.isDefault);
        freightModelSimpleInfoRetVo.setName(this.getName());
        freightModelSimpleInfoRetVo.setType(this.getType());
        return freightModelSimpleInfoRetVo;
    }
}
