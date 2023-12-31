package com.study.crm.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 在Cookie中获取用户id工具类
 */
public class LoginUserUtil {

    /**
     * 从cookie中获取userId
     * @param request
     * @return
     */
    public static int releaseUserIdFromCookie(HttpServletRequest request) {
        String userIdString = CookieUtil.getCookieValue(request, "userIdStr");
        System.out.println(userIdString);
        if (StringUtils.isBlank(userIdString)) {
            return 0;
        }
        Integer userId = UserIDBase64.decoderUserID(userIdString);
       // System.out.println(userId);
        return userId;
    }
}
