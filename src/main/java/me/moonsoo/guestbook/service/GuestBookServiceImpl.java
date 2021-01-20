package me.moonsoo.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonsoo.guestbook.dto.GuestBookDTO;
import me.moonsoo.guestbook.entity.GuestBook;
import me.moonsoo.guestbook.repository.GuestbookRepository;
import org.springframework.stereotype.Service;

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
}
