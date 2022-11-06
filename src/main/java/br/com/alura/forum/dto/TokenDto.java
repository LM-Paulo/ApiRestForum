package br.com.alura.forum.dto;

public class TokenDto {

    private String token;
    private String bearer;

    public TokenDto(String token, String bearer) {
        this.bearer = bearer;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getbearer() {
        return bearer;
    }

    public void setbearer(String tipo) {
        this.bearer = tipo;
    }


}
