package ru.demo.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.demo.library.models.Book;
import ru.demo.library.models.Person;

import java.util.List;
import java.util.Optional;

/**
 * Этот класс является примером реализации DAO (Data Access Object) для работы с базой данных,
 * конкретно с таблицей ‘Person’. Он использует JdbcTemplate для выполнения SQL-запросов к базе данных.
 */
@Component
public class PersonDAO {
    // предоставляет методы для выполнения различных операций с базой данных
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Выполняет запрос на получение всех записей из таблицы ‘Person’ и возвращает их в виде списка объектов ‘Person’.
     */
    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    /**
     * Выполняет запрос для получения информации об одном человеке по его идентификатору
     * и возвращает объект ‘Person’ или null, если такой человек не найден.
     * @param id - ID пользователя
     */
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    /**
     * Добавляет в базу данных нового человека
     * @param person - Обновленный объект человека
     */
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(FIO,age) VALUES(?, ?)", person.getFIO(), person.getAge());
    }

    /**
     * Обновляет информацию о человеке в базе данных, изменяя его ФИО и возраст.
     * @param id - ID обновляемого пользователя
     * @param updatedPerson - Обновленный объект пользователя
     */
    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET FIO=?, age=? WHERE id=?", updatedPerson.getFIO(),
                updatedPerson.getAge(), id);
    }

    /**
     * Удаляет пользователя из базы данных по ID
     * @param id - ID удаляемой книги
     */
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    /**
     * Находит пользователя в базе данных по ФИО
     * @param fio - ФИО для поиска
     * @return - объект класса Optional, который может содержать значение, а может и не содержать
     */
    public Optional<Person> getPersonByFullName(String fio) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE fio=?", new Object[]{fio},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    /**
     * Находит все книги назначенные пользователю
     * @param id - ID книги
     * @return - результаты запроса преобразуются в объекты Book с помощью BeanPropertyRowMapper и возвращаются.
     */
    public List<Book> getBookByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
