package org.choon.crudprc.member.dto.resp;

import org.choon.crudprc.member.entity.Member;

public class MeRespDto {
    private Member member;

    public Member getMember() {
        return member;
    }

    private MeRespDto(Member member) {
        this.member = member;
    }

    public static MeRespDto from(Member member) {
        return new MeRespDto(member);
    }
}
