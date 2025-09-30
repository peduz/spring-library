package org.lessons.spring_library.repository;

import org.lessons.spring_library.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer>{

}
