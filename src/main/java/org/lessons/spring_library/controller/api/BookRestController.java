package org.lessons.spring_library.controller.api;

import java.util.List;

import org.lessons.spring_library.model.Book;
import org.lessons.spring_library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getMethodName(@RequestParam(name = "keyword", required = false) String param) {
        List<Book> result = null;
        if (param != null && param.equals("errore")) {
            throw new IllegalArgumentException("Parametro errato");
        } else if (param != null && !param.isBlank()) {
            result = bookRepository.findByPublisherContainingIgnoreCase(param);
        } else {
            result = bookRepository.findAll();
        }
        return result;
    }

    @GetMapping("{id}")
    public Book get(@PathVariable("id") Integer id) {
        return bookRepository.findById(id).get();
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }


    @PutMapping("{id}")
    public Book put(@PathVariable Integer id, @RequestBody Book entity) {
        
        return bookRepository.save(entity);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        bookRepository.deleteById(id);
    }

}
