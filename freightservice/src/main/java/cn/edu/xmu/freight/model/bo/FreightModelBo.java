package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.freight.model.vo.FreightModelRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:37
 */
@Data
public class FreightModelBo implements VoObject, Serializable {
    private Long id;
    private String name;
    private Byte type;
    //@JsonProperty(value = "default")
    private Boolean isDefault;
    private String gmtCreate;
    private String gmtModified;

    public FreightModelBo()
    {

    }

    public FreightModelBo(FreightModelPo freightModelPo){
        this.setId(freightModelPo.getId());
        this.setName(freightModelPo.getName());
        this.setGmtCreate(freightModelPo.getGmtCreate().toString());
        this.setGmtModified(freightModelPo.getGmtModified().toString());
        if(freightModelPo.getDefaultModel()==1){
            this.setIsDefault(Boolean.TRUE);
        }else{
            this.setIsDefault(Boolean.FALSE);
        }

        this.setType(freightModelPo.getType());
    }
    @Override
    public Object createVo() {
        FreightModelRetVo freightModelRetVo = new FreightModelRetVo(this);
        return freightModelRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
