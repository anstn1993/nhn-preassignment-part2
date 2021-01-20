package me.moonsoo.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonsoo.guestbook.dto.GuestBookDTO;
import me.moonsoo.guestbook.dto.PageRequestDTO;
import me.moonsoo.guestbook.dto.PageResultDTO;
import me.moonsoo.guestbook.entity.GuestBook;
import me.moonsoo.guestbook.repository.GuestbookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        Page<GuestBook> result = guestbookRepository.findAll(pageable);
        Function<GuestBook, GuestBookDTO> fn = entity -> entityToDto(entity);
        return new PageResultDTO<>(result, fn);
    }
}
