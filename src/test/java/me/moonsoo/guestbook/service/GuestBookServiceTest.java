package me.moonsoo.guestbook.service;

import me.moonsoo.guestbook.dto.GuestBookDTO;
import me.moonsoo.guestbook.dto.PageRequestDTO;
import me.moonsoo.guestbook.dto.PageResultDTO;
import me.moonsoo.guestbook.entity.GuestBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestBookServiceTest {

    @Autowired
    private GuestBookService guestBookService;

    @Test
    public void testRegister() {
        GuestBookDTO dto = GuestBookDTO.builder()
                .title("Sample Tiele...")
                .content("Sample Content...")
                .writer("user0")
                .build();

        Long registeredGno = guestBookService.register(dto);
        assertThat(registeredGno).isNotNull();
    }

    @Test
    public void testList() {
        PageRequestDTO requestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO = guestBookService.getList(requestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("---------------------------------");

        for (GuestBookDTO guestBookDTO : resultDTO.getDtoList()) {
            System.out.println(guestBookDTO);
        }

        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testSearch() {

        PageRequestDTO requestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")//제목 + 내용
                .keyword("한글")
                .build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO = guestBookService.getList(requestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("-------------------------------------");
        for(GuestBookDTO guestBookDTO : resultDTO.getDtoList()) {
            System.out.println(guestBookDTO);
        }

        System.out.println("=====================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}