package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.po.FreightModelPo;
import cn.edu.xmu.freight.model.vo.FreightModelRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private Integer unit;
    private Boolean isDefault;
    private String gmtCreate;
    private String gmtModified;

    /**
     * @author issyu 30320182200070
     * @date 2020/12/8 1:43
     * json
     */
    public FreightModelBo()
    {

    }

    /**
     * @author issyu 30320182200070
     * @dare 2020/12/8 1:43
     * @param freightModelPo
     */
    public FreightModelBo(FreightModelPo freightModelPo){
        this.setUnit(freightModelPo.getUnit());
        this.setName(freightModelPo.getName());
        this.setGmtModified(freightModelPo.getGmtModified().toString());
        this.setGmtCreate(freightModelPo.getGmtCreate().toString());
        this.setId(freightModelPo.getId());
        this.setType(freightModelPo.getType());
        if(freightModelPo.getDefaultModel()==1){
            this.setIsDefault(Boolean.TRUE);
        }else{
            this.setIsDefault(Boolean.FALSE);
        }
    }

    @Override
    public Object createVo() {
        FreightModelRetVo freightModelRetVo = new FreightModelRetVo(this);
        return freightModelRetVo;
    }

    /**
     * 大统一
     * @return
     */
    @Override
    public Object createSimpleVo() {
        return null;
    }
}
