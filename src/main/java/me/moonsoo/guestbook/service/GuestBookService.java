package me.moonsoo.guestbook.service;

import me.moonsoo.guestbook.dto.GuestBookDTO;
import me.moonsoo.guestbook.entity.GuestBook;

public interface GuestBookService {
    Long register(GuestBookDTO dto);

    default GuestBook dtoToEntity(GuestBookDTO dto) {
        return GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }
}
