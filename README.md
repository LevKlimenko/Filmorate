# java-filmorate
Template repository for Filmorate project.

Схема представляет из себя первоначальную логику приложения Filmorate, в которой реализовано добавление данных о пользователе и фильме, проставление "лайков" и добавление друзей. в ФЗ задано реализовать "статусы" добавление друзей, а также добавить возможность выбрать жанры и установить рейтинг фильму.
В схеме это отображено соответствующими таблицами.

На данном этапе изучения методов получения данных из SQL мы можем использовать следующие команды для получения информации из таблиц:

Получения данных по ID:

SELECT *  
FROM user  
WHERE id = 2;  


SELECT *  
FROM film  
WHERE id = 1;  

---------------------------

получение жанра:

SELECT fg.name
FROM film_genre AS fg  
WHERE f.id = N  
INNER JOIN film AS f ON fg.film_id=f.id  

---------------------------

получение ТОП 10 фильмов:

SELECT f.name,
COUNT (l.user_id) AS c
FROM film AS f
LEFT OUTER JOIN film_like AS l ON l.film_id=f.id
GROUP BY l.film_id
ORDER BY c DESC
LIMIT 10;

---------------------------

получение друзей пользователя:  
SELECT * 
FROM user AS u  
RIGHT JOIN  user_friend AS fr ON fr.user1_id=u.id  
LEFT JOIN user AS u2 ON fr.user2_id = u2.id  
WHERE u.id = 1

![This is an image](https://github.com/LevKlimenko/java-filmorate/blob/main/Sprint11SchemeWithRate.png?raw=true)
