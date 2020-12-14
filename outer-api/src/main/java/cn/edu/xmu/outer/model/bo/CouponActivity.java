package cn.edu.xmu.outer.model.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivity {
    private Long id;

    private String name;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
