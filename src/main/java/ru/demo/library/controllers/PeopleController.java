package ru.demo.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.demo.library.dao.PersonDAO;
import ru.demo.library.models.Person;
import ru.demo.library.util.PersonValidator;

import javax.validation.Valid;

/**
 * Контроллер для обрабатывания запросов с пользователями.
 */

@Controller
@RequestMapping("/people")
public class PeopleController {
    // предоставляет методы для работы с данными людей.
    private final PersonDAO personDAO;
    // предоставляет методы для проверки достоерности данных пользователя
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    /**
     * Этот метод используется для получения списка всех пользователей из базы данных.
     * Он добавляет всех пользователей в модель под ключом “people”.
     * @param model - используется для передачи данных между контроллером и представлением.
     * @return - возвращает имя шаблона представления (“people/index”),который будет использоваться
     * для отображения списка всех пользователей.
     */
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    /**
     * Метод добавляет в модель пользователя и кингу через параметр id
     * @param id - ID  person
     * @param model - используется для передачи данных между контроллером и представлением.
     * @return -  возвращает имя шаблона представления (“people/show”)
     */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("books", personDAO.getBookByPersonId(id));
        return "people/show";
    }

    /**
     * Этот метод создает нового пользователя и добавляет его в модель под ключом “person”.
     * @param person - объект Person с ключом “person”.
     * @return - имя шаблона представления (“people/new”),
     */
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    /**
     * Этот метод обрабатывает отправку формы для создания нового пользователя и проверяет его с помощью валидатора.
     * @param person - объект Person с ключом “person”.
     * @param bindingResult - обработчик ошибок валидации.
     * @return - перенаправляет на страницу со списком всех пользователей
     */
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }

    /**
     * Этот метод редактирует пользователя с заданным ID.
     * @param model - используется для передачи данных между контроллером и представлением.
     * @param id - ID книги, который используется для получения книги из базы данных и выполнения операций над ней.
     * @return - возвращается шаблон представления “people/edit”.
     */
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }


    /**
     * Этот метод обновляет пользователя с указанным ID.
     * @param person - объект Person с ключом “person”.
     * @param bindingResult - обработчик ошибок валидации.
     * @param id - ID пользователя, который используется для получения пользователя из базы данных
     * @return - перенаправляет на страницу со списком всех пользователей
     */
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(id, person);
        return "redirect:/people";
    }

    /**
     * Этот метод удаляет пользователя с указанным ID из базы данных.
     * @param id - ID пользователя, который используется для получения пользователя из базы данных
     * @return - перенаправляет на страницу со списком всех пользователей.
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
