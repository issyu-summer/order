package cn.edu.xmu.external.bo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSegInfo {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
