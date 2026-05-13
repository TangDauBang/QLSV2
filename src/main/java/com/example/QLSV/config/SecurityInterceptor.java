package com.example.QLSV.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // 📌 Cho phép truy cập các trang công khai
        if (isPublicPage(requestURI)) {
            return true;
        }

        // 📌 Kiểm tra xem user đã đăng nhập chưa
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            // Nếu chưa đăng nhập, redirect đến login
            response.sendRedirect("/login");
            return false;
        }

        // 📌 Lấy role của user
        String role = (String) session.getAttribute("role");

        // 📌 Kiểm tra quyền truy cập dựa trên URL
        if (!hasPermission(requestURI, role)) {
            response.sendRedirect("/access-denied");
            return false;
        }

        return true;
    }

    /**
     * Kiểm tra xem URL có phải trang công khai không
     * Những trang này có thể truy cập mà không cần đăng nhập
     */
    private boolean isPublicPage(String uri) {
        return uri.equals("/") || 
               uri.equals("/login") || 
               uri.startsWith("/static/") ||
               uri.equals("/logout") ||
               uri.equals("/access-denied");
    }

    /**
     * Kiểm tra xem user có quyền truy cập URL hay không
     */
    private boolean hasPermission(String uri, String role) {
        if (role == null) {
            return false;
        }

        // Admin có quyền truy cập mọi trang
        if ("ADMIN".equals(role)) {
            return true;
        }

        // Kiểm tra quyền cho từng role cụ thể
        if (uri.startsWith("/admin")) {
            return "ADMIN".equals(role);
        } else if (uri.startsWith("/giangvien")) {
            return "GIANG_VIEN".equals(role);
        } else if (uri.startsWith("/sinhvien")) {
            return "SINH_VIEN".equals(role);
        }

        // Cho phép truy cập các trang khác (không thuộc role cụ thể)
        return true;
    }
}
