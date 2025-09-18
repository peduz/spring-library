package org.lessons.spring_library.controller;

import java.util.List;
import java.util.Optional;

import org.lessons.spring_library.model.Book;
import org.lessons.spring_library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    public String index(Model model, @RequestParam(name="keyword", required=false) String keyword) {
        List<Book> result = null;
        if(keyword == null || keyword.isBlank()) {
            result = repository.findAll();
        } else {
            result = repository.findByPublisherContainingIgnoreCase(keyword);
        }
        model.addAttribute("list", result);
        return "books/index";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        Optional<Book> optionalBook = repository.findById(id);
        if(optionalBook.isPresent()) {
            model.addAttribute("book", optionalBook.get());
            model.addAttribute("empty", false);
        } else {
            model.addAttribute("empty", true);
        }
        return "/books/show";
    }

}
