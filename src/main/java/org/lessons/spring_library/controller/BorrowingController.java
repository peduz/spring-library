package org.lessons.spring_library.controller;

import org.lessons.spring_library.model.Borrowing;
import org.lessons.spring_library.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingRepository repository;

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("borrowing") Borrowing borrowing, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "borrowings/edit";
        }

        repository.save(borrowing);

        return "redirect:/books/show/" + borrowing.getBook().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Borrowing borrowing = repository.findById(id).get();
        model.addAttribute("editMode", true);
        model.addAttribute("borrowing", borrowing);
        return "/borrowings/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("borrowing") Borrowing borrowing, BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "/borrowings/edit";
        }
        repository.save(borrowing);
        return "redirect:/books/show/" + borrowing.getBook().getId();
    }

}
