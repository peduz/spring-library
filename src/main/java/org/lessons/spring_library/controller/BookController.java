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
        if (optBook.isPresent()) {
            //qui vuol dire che ha trovato un libro con ISBN su db
            bindingResult.addError(new ObjectError("isbn", "Isbn already present"));
        }

        if (formBook.getYear() != null &&
            formBook.getYear() > LocalDate.now().getYear()) {
            bindingResult.addError(new ObjectError("year", "Year cannot be in the future"));
        }

        if (bindingResult.hasErrors()) {
            return "/books/create";
        }

        repository.save(formBook);
        redirectAttributes.addFlashAttribute("successMessage", "Book created successifully");
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<Book> optBook = repository.findById(id);
        Book book = optBook.get();
        model.addAttribute("book", book);

        return "/books/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("book") Book formBook, BindingResult bindingResult,
            Model model) {

        Book oldBook = repository.findById(formBook.getId()).get();

        if (!oldBook.getTitle().equals(formBook.getTitle())) {
            bindingResult.addError(new ObjectError("title", "Cannot change the title!"));
        }

        if (!oldBook.getIsbn().equals(formBook.getIsbn())) {
            bindingResult.addError(new ObjectError("isbn", "Cannot change ISBN!"));
        }

        if (formBook.getYear() > LocalDate.now().getYear()) {
            bindingResult.addError(new ObjectError("year", "Year cannot be in the future"));
        }

        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }

        repository.save(formBook);

        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {

        repository.deleteById(id);

        return "redirect:/books";
    }
}
