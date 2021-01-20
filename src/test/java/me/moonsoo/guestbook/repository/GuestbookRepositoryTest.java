package me.moonsoo.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import me.moonsoo.guestbook.entity.GuestBook;
import me.moonsoo.guestbook.entity.QGuestBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class GuestbookRepositoryTest {
    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            GuestBook guestBook = GuestBook.builder()
                    .title("Title...." + i)
                    .content("Content...." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestBook));
        });
    }

    @Test
    public void updateTest() {
        Optional<GuestBook> result = guestbookRepository.findById(300L);

        if(result.isPresent()) {
            GuestBook guestBook = result.get();
            //수정
            guestBook.changeTitle("Changed Title....");
            guestBook.changeContent("Changed Content....");

            guestbookRepository.save(guestBook);
        }
    }

    @Test
    public void testQuery1() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno"));

        QGuestBook qGuestBook = QGuestBook.guestBook;//테이블 컬럼별로 접근하여 조건을 주기 위한 객체

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();//where조건절을 모두 담는 컨테이너로 predicate인터페이스의 구현체다.
        BooleanExpression expression = qGuestBook.title.contains(keyword);//조건절 표현식으로 predicate인터페이스의 구현체다.
        builder.and(expression);//builder에 조건식을 하나씩 추가할 수 있다. 동적 쿼리의 핵심이다.

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable);//이 메서드는 QuerydslPredicateExecutor인터페이스의 메서드
        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exGno = qGuestBook.gno.gt(0L);
        BooleanExpression exTitle = qGuestBook.title.contains(keyword);
        BooleanExpression exContent = qGuestBook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);// title이나 content에 keyword가 포함되는 경우
        builder.and(exGno).and(exAll);//gno번호가 0보다 크면서 title이나 content에 keyword가 포함되는 경우

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable);
        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }
}