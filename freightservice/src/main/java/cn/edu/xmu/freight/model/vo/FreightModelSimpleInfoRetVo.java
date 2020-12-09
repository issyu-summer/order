package cn.edu.xmu.freight.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/*
 * @author 史韬韬
 * @date 2020/12/7
 * 运费模板概要信息返回类
 */
@Data
public class FreightModelSimpleInfoRetVo {
    private Long id;
    private String name;
    private Byte type;
    private Boolean isDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
