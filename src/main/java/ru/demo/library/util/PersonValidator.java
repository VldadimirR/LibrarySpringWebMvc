package ru.demo.library.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.demo.library.dao.PersonDAO;
import ru.demo.library.models.Person;

/**
 * Этот класс является валидатором для объектов класса Person.
 * Он проверяет, существует ли уже человек с таким ФИО в базе данных. Если такой человек существует,
 * то метод rejectValue добавляет сообщение об ошибке в коллекцию ошибок (Errors).
 * В противном случае проверка проходит успешно.
 */
@Component
public class PersonValidator implements Validator {

    // предоставляет методы для работы с данными людей.
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    /**
     * Этот метод определяет, поддерживает ли данный класс валидацию.
     * @param aClass the {@link Class} that this {@link Validator} is
     * being asked if it can {@link #validate(Object, Errors) validate}
     */
    @Override
    public boolean supports(Class<?> aClass){
        return Person.class.equals(aClass);
    }

    /**
     * Этот метод выполняет проверку, существует ли уже в базе данных человек с таким же ФИО, как у объекта person
     * Если такой человек есть, то метод rejectValue добавляет сообщение об ошибке.
     * В противном случае проверка считается успешной.
     * @param o the object that is to be validated
     * @param errors contextual state about the validation process
     */

    @Override
    public void validate(Object o, Errors errors){
        Person person = (Person) o;

        if (personDAO.getPersonByFullName(person.getFIO()).isPresent())
            errors.rejectValue("FIO", "", "Человек с таким ФИО уже существует");
    }
}
