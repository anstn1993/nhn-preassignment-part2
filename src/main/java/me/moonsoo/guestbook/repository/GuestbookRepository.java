package me.moonsoo.guestbook.repository;

import me.moonsoo.guestbook.entity.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GuestbookRepository extends JpaRepository<GuestBook, Long>, QuerydslPredicateExecutor<GuestBook> {
}
