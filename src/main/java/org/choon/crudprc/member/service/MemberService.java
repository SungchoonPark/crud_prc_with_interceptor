package org.choon.crudprc.member.service;

import org.choon.crudprc.exception.LoginException;
import org.choon.crudprc.exception.UserException;
import org.choon.crudprc.member.dto.req.LoginReqDto;
import org.choon.crudprc.member.dto.resp.LoginRespDto;
import org.choon.crudprc.member.dto.resp.MeRespDto;
import org.choon.crudprc.member.entity.Member;
import org.choon.crudprc.member.repository.MemberRepository;
import org.choon.crudprc.util.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public LoginRespDto login(LoginReqDto loginReqDto) {
        Member member = memberRepository.findByUsername(loginReqDto.getUsername())
                .orElseThrow(() -> new LoginException("로그인 실패!!"));

        if (!member.getPassword().equals(loginReqDto.getPassword())) {
            throw new LoginException("로그인 실패!!");
        }

        String accessToken = jwtProvider.createToken(member.getName());
        return LoginRespDto.of(member.getName(), accessToken);
    }

    public MeRespDto profile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserException("이런 멤버 없다"));

        return MeRespDto.from(member);
    }
}
