package org.choon.crudprc.member.dto.resp;

public class LoginRespDto {
    private String name;
    private String accessToken;

    private LoginRespDto(String name, String accessToken) {
        this.name = name;
        this.accessToken = accessToken;
    }

    public static LoginRespDto of(String name, String accessToken) {
        return new LoginRespDto(name, accessToken);
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
