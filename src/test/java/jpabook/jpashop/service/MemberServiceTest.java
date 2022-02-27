package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long memberId = memberService.join(member); //persist 까지는 insert문 X, commit될때 flush 하면서 insert문
        //Transactional 은 "테스트 케이스"에서 기본적으로 rollback 진행함 -> insert문 X, 영속성 컨텍스트 flush 하지 않음
        //@Rollback(false) 사용하면 insert문 진행 or em.flush() 사용

        //then
        assertEquals(member, memberRepository.findOne(memberId)); //같은 영속성 컨텍스트 안에 있는것을 가져오기 때문에 같음
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        //JUnit5에서는 @Test(expected = ~~) 지원하지 않음, 아래와 같이 테스트 진행함
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }
}