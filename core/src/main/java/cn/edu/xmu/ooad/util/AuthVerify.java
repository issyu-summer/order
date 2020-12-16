package cn.edu.xmu.ooad.util;

/**
 * 身份校验工具类
 * @author issyu 30320182200070
 * @date 2020/12/15 21:43
 */
public class AuthVerify {

/**
 * 买家本人名下校验 for orders
 * @author issyu 30320182200070
 * @date 2020/12/16 12:18
 */
public static Boolean customerAuth(Long userId){
    Long id = -2L;
    if(userId.equals(id)){
        return Boolean.TRUE;
    }else {
        return Boolean.FALSE;
    }
}

/**
 * 管理员超级权限
 * @author issyu 30320182200070
 * @date 2020/12/16 12:42
 */
public static Boolean adminAuth(Long departId){
    if(departId.equals(0L)){
        return Boolean.TRUE;
    }else {
        return Boolean.FALSE;
    }
}

/**
 * 没有店铺的店家，什么事情也做不了
 * @author issyu 30320182200070
 * @date 2020/12/16 12:51
 */
public static Boolean noShopAdminAuth(Long departId){
    Long id=-1L;
    if(departId.equals(id)){
        return Boolean.TRUE;
    }else{
        return Boolean.FALSE;
    }
}

/**
 * 拥有店铺的店家
 * @author issyu 30320182200070
 * @date 2020/12/16 13:02
 */
public static Boolean shopAdminAuth(Long departId){
    Long id = 0L;
    if(departId.compareTo(id)>0){
        return Boolean.TRUE;
    }else {
        return Boolean.FALSE;
    }
}

}
