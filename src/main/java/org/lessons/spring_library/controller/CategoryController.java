package org.lessons.spring_library.controller;

import org.lessons.spring_library.model.Book;
import org.lessons.spring_library.model.Category;
import org.lessons.spring_library.repository.CategoryRepository;
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

import jakarta.validation.Valid;




@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository repository;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("list", repository.findAll());
        model.addAttribute("categoryObj", new Category());
        return "categories/index";
    }
    
    @PostMapping("/create")
    public String postMethodName(@Valid @ModelAttribute("categoryObj") Category category,
                BindingResult bindingResult, Model model) {
        Category cat = repository.findByCategory(category.getCategory());
        if(cat == null) {
            //se NON esiste una categoria con quel nome
        } else {
            //se esiste una categoria con quel nome
            bindingResult.addError(new ObjectError("category", "Category already present"));
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("list", repository.findAll());
            return "categories/index";
        }
        repository.save(category);

        return "redirect:/categories";
    }
    
    @PostMapping("/delete/{id}")
    public String requestMethodName(@PathVariable Integer id, Model model) {
        Category cat = repository.findById(id).get();
        for(Book book : cat.getBooks()) {
            book.getCategories().remove(cat);
        }
        repository.deleteById(id);
        return "redirect:/categories";
    }
    
}
