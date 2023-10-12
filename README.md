# Restaurant Pet Project

## Описание
Это демонстрационный проект для веб-сайта ресторана, созданный с использованием Spring Boot.

## Предварительные требования
- Java 17
- MySQL Database

## Шаги по установке

1. **Клонировать репозиторий:**
   ```bash
   git clone https://github.com/Joroboro253/Restaurant_spring_pet_project.git
 
2. **Перейти в директорию проекта:**
  cd Restaurant_spring_pet_project

3. **Собрать проект с помощью Maven**:
  ./mvnw clean install
   
4. **Настройка базы данных:**
  - Убедитесь, что MySQL запущен на вашем компьютере.
  - Создайте базу данных с именем restaurant.
  - Обновите файл src/main/resources/application.properties своими учетными данными для базы данных, если они отличаются от стандартных:
    **spring.datasource.username=ВАШЕ_ИМЯ_ПОЛЬЗОВАТЕЛЯ**
    **spring.datasource.password=ВАШ_ПАРОЛЬ**

5. **Запустите приложение:**
  ./mvnw spring-boot:run
  Перейдите к приложению по адресу http://localhost:8080.

  ## Использование
  После запуска приложения вы можете перейти на главную страницу и изучить предложения ресторана, управлять блюдами, пользователями и многим другим.
