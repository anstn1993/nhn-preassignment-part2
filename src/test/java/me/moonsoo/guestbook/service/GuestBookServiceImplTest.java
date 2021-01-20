package me.moonsoo.guestbook.service;

import me.moonsoo.guestbook.dto.GuestBookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestBookServiceImplTest {

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
}