package cn.edu.xmu.freight.model.bo;

import cn.edu.xmu.freight.model.vo.FreightModelRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

/**
 * @author issyu 30320182200070
 * @date 2020/12/5 1:37
 */
@Data
public class FreightModelBo implements VoObject {
    private Long id;
    private String name;
    private Byte type;
    private Boolean isDefault;
    private String gmtCreate;
    private String gmtModified;

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
