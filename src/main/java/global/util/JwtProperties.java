package global.util;

public interface JwtProperties {

    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_HEADER_STRING = "Authorization";
    String REFRESH_HEADER_STRING = "RefreshToken";
    String ROLE = "role";
    String MEMBER_ID_KEY = "id";
}