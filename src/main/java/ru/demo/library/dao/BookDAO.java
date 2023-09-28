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
 * BookDAO реализует методы для работы с сущностью Book.
 * Он использует JdbcTemplate для взаимодействия с базой данных и выполняет различные операции,
 * такие как получение всех книг из таблицы Book, получение конкретной книги по ее ID, а также сохранение
 * новой книги в базе данных.
 */
@Component
public class BookDAO {
    // предоставляет методы для выполнения различных операций с базой данных
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * BeanPropertyRowMapper используется для преобразования результатов запроса в объекты Book.
     * @return - возвращает список объектов Book
     */
    public List<Book> index(){
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    /**
     * @param id - ID требуемой книги
     * @return - возвращает объектов Book с нужным ID
     */
    public Book show(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    /**
     * Добавляет в базу данных новую книгу
     * @param book - передаваемый объект книги Book
     */
    public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book(name,author,year) VALUES (?, ?, ?)", book.getName(),
                book.getAuthor(), book.getYear());
    }

    /**
     * Изменяет название, автора, год книги в базе данных
     * @param id - ID для нахождения книги в базе данных
     * @param bookUpdate - Измененный объект Книги
     */
    public void update(int id, Book bookUpdate) {
        jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=? WHERE id=?", bookUpdate.getName(),
                bookUpdate.getAuthor(), bookUpdate.getYear(), id);
    }

    /**
     * Удаляет книгу из базы данных по ID
     * @param id - ID удаляемой книги
     */
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    /**
     * Находит все книги назначенные пользователю с помощью JOIN и поля person_id
     * @param id - ID пользователя
     */
    public Optional<Person> getBookOwner(int id) {
        return  jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.id " +
                "WHERE Book.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    /**
     * Убирает из базы данных информацию о нахождении у пользователя книги
     * @param id - ID книги
     */
    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
    }

    /**
     * Назначает книгу пользователю с помощью person_id
     * @param id - ID Книги
     * @param selectedPerson - Пользователь, которому назначается книга
     */
    public void assign(int id, Person selectedPerson) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", selectedPerson.getId(), id);
    }
}
