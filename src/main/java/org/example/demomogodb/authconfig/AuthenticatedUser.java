//package org.example.demomogodb.authconfig;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import java.util.Objects;
//
//@Getter
//@Setter
//public class AuthenticatedUser extends UsernamePasswordAuthenticationToken {
//    private final String code;
//
//    public AuthenticatedUser(String code) {
//        super(null, null, null);
//        this.code = code;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        AuthenticatedUser that = (AuthenticatedUser) obj;
//        return Objects.equals(code, that.code);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(code);
//    }
//}
