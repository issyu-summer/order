package cn.edu.xmu.freight.model.vo;

import cn.edu.xmu.freight.model.bo.FreightModelBo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 运费模板返回值对象
 * @author issyu 30320182200070
 * @date 2020/12/5 1:34
 */
@Data
public class FreightModelRetVo {
    private Long id;
    private String name;
    private Byte type;
    @JsonProperty(value = "default")
    private Boolean isDefault;
    private String gmtCreate;
    private String gmtModified;

    /*
    @JsonProperty("default")
    public String getIsDefault(){
        return this.isDefault;
    }
    */

    public FreightModelRetVo(FreightModelBo freightModelBo){
        this.setGmtCreate(freightModelBo.getGmtCreate());
        this.setGmtModified(freightModelBo.getGmtModified());
        this.setId(freightModelBo.getId());
        this.setIsDefault(freightModelBo.getIsDefault());
        this.setName(freightModelBo.getName());
        this.setType(freightModelBo.getType());
    }

}
