package br.ufpb.dsc.expense_tracker_api.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import br.ufpb.dsc.expense_tracker_api.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // extrai o token

            try {
                Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token).getBody();
                int userId = Integer.parseInt(claims.get("userId").toString());
                httpRequest.setAttribute("userId", userId);
                // Aqui você pode configurar o contexto de segurança, se necessário
            } catch (ExpiredJwtException e) {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Token expirado");
                return;
            } catch (SignatureException e) {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Assinatura inválida");
                return;
            } catch (Exception e) {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Token inválido");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Token de autorização deve ser fornecido");
            return;
        }
        chain.doFilter(request, response); // continue o processo
    }
}
