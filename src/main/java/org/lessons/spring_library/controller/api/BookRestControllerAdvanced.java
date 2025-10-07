package org.lessons.spring_library.controller.api;

import java.util.List;
import java.util.Optional;

import org.lessons.spring_library.model.Book;
import org.lessons.spring_library.model.GenericPayload;
import org.lessons.spring_library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v2/books")
public class BookRestControllerAdvanced {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getMethodName(@RequestParam(name = "keyword", required = false) String param) {
        List<Book> result = null;
        if (param != null && param.equals("errore")) {
            // throw new IllegalArgumentException("Parametro errato");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        } else if (param != null && !param.isBlank()) {
            result = bookRepository.findByPublisherContainingIgnoreCase(param);
        } else {
            result = bookRepository.findAll();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<GenericPayload<Book>> get(@PathVariable("id") Integer id) {
        Optional<Book> bookOpt = bookRepository.findById(id);

        if(bookOpt.isPresent()) {
            GenericPayload<Book> result = new GenericPayload<>(bookOpt.get(), 
            "", HttpStatus.OK.getReasonPhrase());

            return new ResponseEntity<GenericPayload<Book>>(result, HttpStatus.OK);
        } else {
            GenericPayload<Book> result = new GenericPayload<>(null, 
            "Il libro con id " + id + " non esiste", HttpStatus.BAD_REQUEST.getReasonPhrase());

            return new ResponseEntity<GenericPayload<Book>>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }
}
