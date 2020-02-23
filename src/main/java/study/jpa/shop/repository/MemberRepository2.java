package study.jpa.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.jpa.shop.domain.Member;

import java.util.List;

public interface MemberRepository2 extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
}
