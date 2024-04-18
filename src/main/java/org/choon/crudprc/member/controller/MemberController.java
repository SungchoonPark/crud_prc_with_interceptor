package org.choon.crudprc.member.controller;

import org.choon.crudprc.annotation.NoAuth;
import org.choon.crudprc.member.dto.req.LoginReqDto;
import org.choon.crudprc.member.dto.resp.LoginRespDto;
import org.choon.crudprc.member.dto.resp.MeRespDto;
import org.choon.crudprc.member.service.MemberService;
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
    public ResponseEntity<LoginRespDto> login(@RequestBody LoginReqDto loginReqDto) {
        LoginRespDto resp = memberService.login(loginReqDto);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<MeRespDto> profile(@RequestParam Long id) {
        MeRespDto resp = memberService.profile(id);
        return ResponseEntity.ok().body(resp);
    }
}
