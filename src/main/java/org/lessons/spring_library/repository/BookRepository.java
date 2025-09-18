package org.lessons.spring_library.repository;

import java.util.List;

import org.lessons.spring_library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer> {

    public List<Book> findByPublisherContainingIgnoreCase(String publisher);

    @Query(value="select * from Book where id = ?1", nativeQuery=true)
    public Book filtroNativo(Integer id);

    @Query(value="select b from Book b where b.title = ?1")
    public List<Book> filtroHQL(String title);
}
