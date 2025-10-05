package com.librart.managament.repository;

import com.librart.managament.model.IssuedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IssuedBookRepository extends JpaRepository<IssuedBook, String> {
    List<IssuedBook> findByUser_UserId(String userUserId);

    List<IssuedBook> findByUser_UserIdAndReturnDateIsNull(String userId);

    Optional<IssuedBook> findByUser_UserIdAndBook_BookIdAndReturnDateIsNull(String userId, String bookId);

    List<IssuedBook> findByDueDateBeforeAndReturnDateIsNull(LocalDate today);

   List<IssuedBook> findByUser_UserIdAndDueDateBeforeAndReturnDateIsNull(String userId , LocalDate dueDate);

}
