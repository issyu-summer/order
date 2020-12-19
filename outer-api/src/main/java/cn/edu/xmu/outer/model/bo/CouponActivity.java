package cn.edu.xmu.outer.model.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivity implements Serializable {
    private Long id;

    private String name;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
