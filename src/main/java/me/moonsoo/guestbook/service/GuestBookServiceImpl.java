package me.moonsoo.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonsoo.guestbook.dto.GuestBookDTO;
import me.moonsoo.guestbook.dto.PageRequestDTO;
import me.moonsoo.guestbook.dto.PageResultDTO;
import me.moonsoo.guestbook.entity.GuestBook;
import me.moonsoo.guestbook.entity.QGuestBook;
import me.moonsoo.guestbook.repository.GuestbookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor//의존성 자동 주입
public class GuestBookServiceImpl implements GuestBookService {

    private final GuestbookRepository guestbookRepository;//반드시 final로 선언


    @Override
    public Long register(GuestBookDTO dto) {
        log.info("DTO--------------------------");
        log.info(dto);

        GuestBook entity = dtoToEntity(dto);
        log.info(entity);

        GuestBook saved = guestbookRepository.save(entity);
        return saved.getGno();
    }

    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder conditionBuilder = getSearch(requestDTO);
        Page<GuestBook> result = guestbookRepository.findAll(conditionBuilder, pageable);
        Function<GuestBook, GuestBookDTO> fn = entity -> entityToDto(entity);
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestBookDTO read(Long gno) {
        Optional<GuestBook> result = guestbookRepository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void modify(GuestBookDTO dto) {
        Optional<GuestBook> result = guestbookRepository.findById(dto.getGno());
        if(result.isPresent()) {
            GuestBook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            guestbookRepository.save(entity);
        }
    }

    @Override
    public void remove(Long gno) {
        guestbookRepository.deleteById(gno);
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;

        BooleanExpression expression = qGuestBook.gno.gt(0L);//gno > 0

        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) {//검색 조건이 없으면 일반 페이징
            return booleanBuilder;
        }

        //동적으로 조건 추가
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")) {
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")) {
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }
        //gno > 0 조건과 검색 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
