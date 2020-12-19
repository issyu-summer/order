package cn.edu.xmu.external.service;

public interface IAdService {
    Integer resetAdSeg(Long segId);//删除时间段后应当把相应广告的时间段置空
}
