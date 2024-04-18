package org.choon.crudprc.member.controller;

import org.choon.crudprc.annotation.NoAuth;
import org.choon.crudprc.member.dto.req.LoginReqDto;
import org.choon.crudprc.member.dto.resp.LoginRespDto;
import org.choon.crudprc.member.dto.resp.MeRespDto;
import org.choon.crudprc.member.service.MemberService;
import org.choon.crudprc.util.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    @NoAuth
    public ApiUtils.ApiResult<LoginRespDto> login(@RequestBody LoginReqDto loginReqDto) {
        LoginRespDto resp = memberService.login(loginReqDto);
        return ApiUtils.success(resp);
    }

    @GetMapping("/me")
    public ApiUtils.ApiResult<MeRespDto> profile(@RequestParam Long id) {
        MeRespDto resp = memberService.profile(id);
        return ApiUtils.success(resp);
    }
}
