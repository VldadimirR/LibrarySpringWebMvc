package ru.demo.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.demo.library.dao.BookDAO;
import ru.demo.library.dao.PersonDAO;
import ru.demo.library.models.Book;
import ru.demo.library.models.Person;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Контроллер для обрабатывания запросов с книгами.
 */

@Controller
@RequestMapping("/books")
public class BookController {
    // предоставляет методы для работы с данными книг.
    private final BookDAO bookDAO;
    // предоставляет методы для работы с данными людей.
    private final PersonDAO personDAO;


    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    /**
     * Этот метод используется для получения списка всех книг из базы данных.
     * Он добавляет все книги в модель под ключом “books”.
     * @param model - используется для передачи данных между контроллером и представлением.
     * @return - возвращает имя шаблона представления (“books/index”),который будет использоваться
     * для отображения списка всех книг.
     */
    @GetMapping()
    public String indexBook(Model model){
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    /**
     * Этот метод используется для отображения информации о книге с заданным ID.
     * @param id -  ID книги, который используется для получения  книги из базы данных и выполнения операций над ней.
     * @param model - используется для передачи данных между контроллером и представлением.
     * @param person -  объект Person с ключом “person”.
     * @return - возвращает имя шаблона представления (“books/show”).
     */
    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookDAO.show(id));

        Optional<Person> bookOwner = bookDAO.getBookOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personDAO.index());

        return "books/show";
    }

    /**
     * Этот метод создает новую книгу и добавляет ее в модель под ключом “book”.
     * @param book - объект Book с ключом “book”.
     * @return - имя шаблона представления (“books/new”),
     */
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    /**
     * Этот метод обрабатывает отправку формы для создания новой книги и проверяет ее с помощью валидатора.
     * @param book - объект Book с ключом “book”.
     * @param bindingResult - обработчик ошибок валидации.
     * @return - перенаправляет пользователя на страницу со списком всех книг
     */
    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookDAO.save(book);
        return "redirect:/books";
    }


    /**
     * Этот метод редактирует книгу с заданным ID.
     * @param model - используется для передачи данных между контроллером и представлением.
     * @param id - ID книги, который используется для получения книги из базы данных и выполнения операций над ней.
     * @return - возвращается шаблон представления “books/edit”.
     */
    @GetMapping("/{id}/edit")
    public String editBook(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    /**
     * Этот метод обновляет книгу с указанным ID.
     * @param book - объект Book с ключом “book”.
     * @param bindingResult - обработчик ошибок валидации.
     * @param id - ID книги, который используется для получения книги из базы данных и выполнения операций над ней.
     * @return - перенаправляет пользователя на страницу со списком всех книг
     */
    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        bookDAO.update(id, book);
        return "redirect:/books";
    }

    /**
     * Этот метод удаляет книгу с указанным ID из базы данных.
     * @param id - ID книги, который используется для получения  книги из базы данных и выполнения операций над ней.
     * @return - перенаправляет пользователя на страницу со списком всех книг.
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    /**
     * Этот метод удаляет назначенную книгу у пользователя из базы данных.
     * @param id - ID книги, который используется для получения  книги из базы данных и выполнения операций над ней.
     * @return - перенаправляет пользователя на детальную страницу с информацией об этой книге.
     */
    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }

    /**
     * Этот метод назначает книгу пользователю и добавляет информацию в базу данных.
     * @param id - ID книги, который используется для получения книги из базы данных и выполнения операций над ней.
     * @param selectedPerson - объект Person с ключом “person”.
     * @return - перенаправляет пользователя на детальную страницу с информацией об этой книге.
     */
    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,@ModelAttribute("person") Person selectedPerson ){
        bookDAO.assign(id,selectedPerson);
        return "redirect:/books/" + id;
    }

}
