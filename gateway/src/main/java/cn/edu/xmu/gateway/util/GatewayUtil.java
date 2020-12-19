package cn.edu.xmu.gateway.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @title GatewayUtil.java
 * @description 网关工具类
 * @author wwc
 * @date 2020/12/02 17:11
 * @version 1.0
 */
@Component
@Slf4j
public class GatewayUtil {


    @Value("${gateway.jwtExpire:3600}")
    private static Integer jwtExpireTime = 3600;

    @Value("${gateway.refreshJwtTime:60}")
    private static Integer refreshJwtTime = 60;



    public static Integer getJwtExpireTime() {
        return jwtExpireTime;
    }

    public static Integer getRefreshJwtTime() {
        return refreshJwtTime;
    }

    /**
     * 请求类型
     */
    public enum RequestType {
        GET(0, "GET"),
        POST(1, "POST"),
        PUT(2, "PUT"),
        DELETE(3, "DELETE");

        private static final Map<Integer, RequestType> typeMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (RequestType enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        RequestType(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static RequestType getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public static RequestType getCodeByType(HttpMethod method) {
            switch (method) {
                case GET: return RequestType.GET;
                case PUT: return RequestType.PUT;
                case POST: return RequestType.POST;
                case DELETE: return RequestType.DELETE;
                default: return null;
            }
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

    }
}
