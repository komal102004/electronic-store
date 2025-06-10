package com.lcwd.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
 @Autowired
 private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //api se phle chalega jwt: header: usko verify krne ke liye
//        String requestHeader = request.getHeader("Authorization");
        String requestHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Header{}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            // thik h sab
            token = requestHeader.substring(7).trim();
            jwtHelper.validateToken(token);
            try {
                username = jwtHelper.getUserNameFromToken(token);
                log.info("Token Username :{}", username);

            } catch (IllegalArgumentException ex) {
                log.info("Illegal Argument while fetching the username !!"+ex.getMessage());
            }
            catch (ExpiredJwtException ex){
                log.info("Given Jwt is expired !!"+ex.getMessage());
            }
            catch (MalformedJwtException ex){
                log.info("Some changed has done in token !! Invalid Token"+ex.getMessage());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else {
            log.info("Invalid Header !! Header is not starting with Bearer");
        }
        //agar username null nhi hai toh agge proced karenge
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            // username kuch hai
            //authentication null
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
            //valid token
            if(username.equals(userDetails.getUsername())&& !jwtHelper.isTokenExipred(token))
            {
                //token valid
                //security context ke andar authentication set karenge
               UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null);
               authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request,response);
    }
}
