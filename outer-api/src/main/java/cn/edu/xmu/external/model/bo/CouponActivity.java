package cn.edu.xmu.external.model.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponActivity {
    private Long id;

    private String name;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
