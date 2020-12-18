package cn.edu.xmu.gateway.localfilter;

import cn.edu.xmu.gateway.util.GatewayUtil;
import cn.edu.xmu.gateway.util.JwtHelper;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/13 22:31
 **/
public class AuthFilter implements GatewayFilter, Ordered {
    private  static  final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private String tokenName;

    public AuthFilter(Config config){
        this.tokenName = config.getTokenName();
    }

    /**
     * gateway001 权限过滤器
     * 1. 检查JWT是否合法,以及是否过期，如果过期则需要在response的头里换发新JWT，如果不过期将旧的JWT在response的头中返回
     * 2. 判断用户的shopid是否与路径上的shopid一致（0可以不做这一检查）
     * 3. 在redis中判断用户是否有权限访问url,如果不在redis中需要通过dubbo接口load用户权限
     * 4. 需要以dubbo接口访问privilegeservice
     * @param exchange
     * @param chain
     * @return
     * @author wwc
     * @date 2020/12/02 17:13
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求参数
        String token = request.getHeaders().getFirst(tokenName);
        RequestPath url = request.getPath();
        HttpMethod method = request.getMethod();
        // 判断token是否为空，无需token的url在配置文件中设置
        logger.debug("filter: token = " + token);
        if (StringUtil.isNullOrEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.empty());
        }
        // 判断token是否合法
        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        if (userAndDepart == null) {
            // 若token解析不合法
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.empty());
        } else {
            // 若token合法
            // 解析userid和departid和有效期
            Long userId = userAndDepart.getUserId();
            Long departId = userAndDepart.getDepartId();
            Date expireTime = userAndDepart.getExpTime();
            // 检验api中传入token是否和departId一致
            if (url != null) {
                // 获取路径中的shopId
                Map<String, String> uriVariables = exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                String pathId = uriVariables.get("shopid");
                if (pathId != null && !departId.equals(0L)) {
                    // 若非空且解析出的部门id非0则检查是否匹配
                    if (!pathId.equals(departId.toString())) {
                        // 若id不匹配
                        logger.debug("did不匹配:" + pathId);
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.writeWith(Mono.empty());
                    }
                }
                logger.debug("did匹配");
            } else {
                logger.debug("请求url为空");
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.writeWith(Mono.empty());
            }
            String jwt = token;

            // 判断该token有效期是否还长
            Long sec = expireTime.getTime() - System.currentTimeMillis();
            if (sec < GatewayUtil.getRefreshJwtTime() * 1000) {
                // 若快要过期了则重新换发token
                // 创建新的token
                JwtHelper jwtHelper = new JwtHelper();
                jwt = jwtHelper.createToken(userId, departId, GatewayUtil.getJwtExpireTime());
                logger.debug("重新换发token:" + jwt);
            }

            // 将token放在返回消息头中
            response.getHeaders().set(tokenName, jwt);
            // 将url中的数字替换成{id}
            Pattern p = Pattern.compile("/(0|[1-9][0-9]*)");
            Matcher matcher = p.matcher(url.toString());
            String commonUrl = matcher.replaceAll("/{id}");
            // 去除开头的/shops/{id}
            p = Pattern.compile("\\/shops\\/\\{id\\}");
            matcher = p.matcher(commonUrl);
            commonUrl = matcher.replaceFirst("");
            logger.debug("获取通用请求路径:" + commonUrl);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    public static class Config {
        private String tokenName;

        public Config(){

        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }
    }
}
