package org.lessons.spring_library.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.lessons.spring_library.model.Book;
import org.lessons.spring_library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    public String index(Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Book> result = null;
        if (keyword == null || keyword.isBlank()) {
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
        if (optionalBook.isPresent()) {
            model.addAttribute("book", optionalBook.get());
            model.addAttribute("empty", false);
        } else {
            model.addAttribute("empty", true);
        }
        return "/books/show";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("book", new Book());

        return "/books/create";
    }

    @PostMapping("/create")
    public String save(@Valid @ModelAttribute("book") Book formBook, BindingResult bindingResult, 
                        RedirectAttributes redirectAttributes) {
        Optional<Book> optBook = repository.findByIsbn(formBook.getIsbn());
        if(optBook.isPresent()) {
            //qui vuol dire che ha trovato un libro con ISBN su db
            bindingResult.addError(new ObjectError("isbn", "Isbn already present"));
        }

        if(formBook.getYear() > LocalDate.now().getYear()) {
            bindingResult.addError(new ObjectError("year", "Year cannot be in the future"));
        }


        if(bindingResult.hasErrors()) {
            return "/books/create";
        }

        repository.save(formBook);
        redirectAttributes.addFlashAttribute("successMessage", "Book created successifully");
        return "redirect:/books";
    }
    
}
