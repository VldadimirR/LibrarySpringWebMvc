package ru.demo.library.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Этот класс используется для инициализации Spring MVC, когда приложение использует Java-based конфигурацию
 * (через Java annotations). Он позволяет автоматически настраивать и инициализировать Spring MVC,
 * включая создание и настройку DispatcherServlet и связанных компонентов.
 */


public class MySpringMvcDispatcherSerlvetIntitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * Это метод, возвращающий список классов, которые используются для конфигурации приложения.
     * Эти классы содержат информацию о том, как настроить и инициализировать компоненты Spring, такие как BeanFactory,
     * ApplicationContext и другие
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    // Это метод, который возвращает список классов конфигурации для DispatcherServlet.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    // Это метод, который возвращает map-структуру с путями к обрабатываемым сервлетам.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * Это метод, который вызывается при старте Spring-приложения.
     * В нем можно выполнить дополнительные настройки и инициализацию компонентов приложения.
     *
     * @param aServletContext the {@code ServletContext} to initialize - это контекст сервлетов,
     *                        который представляет собой контейнер для объектов сервлетов и других компонентов, связанных с сервлетами
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerCharacterEncodingFilter(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    // это метод, который регистрирует фильтр для обработки скрытых полей в запросах и ответах.
    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
    }

    // Фильтр используется для корректной работы с различными кодировками символов.
    private void registerCharacterEncodingFilter(ServletContext aContext) {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = aContext.addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
    }
}
