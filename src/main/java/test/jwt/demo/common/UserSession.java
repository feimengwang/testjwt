package test.jwt.demo.common;

public class UserSession {

    private String userId;
    private String role;
    private static ThreadLocal<UserSession> threadLocal = new ThreadLocal<>();

    public static void setSessionInfo(UserSession userSession) {
        threadLocal.set(userSession);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
