package cn.edu.xmu.external.model;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class MyReturn<T> extends ReturnObject<T> implements Serializable{
    public MyReturn() {
        super();
    }

    public MyReturn(T data) {
        super(data);
    }

    public MyReturn(ResponseCode code) {
        super(code);
    }

    public MyReturn(ResponseCode code, String errmsg) {
        super(code, errmsg);
    }
}
