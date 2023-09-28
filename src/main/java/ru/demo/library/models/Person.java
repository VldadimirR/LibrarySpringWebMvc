package ru.demo.library.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Этот класс представляет информацию о человеке, включая его уникальный идентификатор,
 * полное имя, возраст и другие данные. Конструктор класса принимает полное имя и возраст,
 * инициализирует соответствующие поля и устанавливает пустой идентификатор
 */

public class Person {
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String FIO;

    @Min(value = 1900, message = "Age should be greater than 1900")
    private int age;

    public Person() {

    }

    public Person(String FIO, int age) {
        this.FIO = FIO;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
