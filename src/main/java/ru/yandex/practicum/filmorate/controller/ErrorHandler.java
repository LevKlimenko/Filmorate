package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmIdException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmNotFoundExceptin;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserIdException;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UserNullException;

import javax.validation.ValidationException;


@RestControllerAdvice
public class ErrorHandler {

   /* @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final IncorrectParametrException e) {
        return new ErrorResponse(String
                .format("Ошибка с \"%s\".", e.getParameter()));
    }
*/
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(final FilmNotFoundExceptin e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmBadId(final FilmIdException e){
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserIdException e){
        return new ErrorResponse(e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNullFound(final UserNullException e){
        return new ErrorResponse(e.getMessage());
    }



    /* @ExceptionHandler
     @ResponseStatus(HttpStatus.CONFLICT)
     public ErrorResponse handleUserAlreadyExist(final UserAlreadyExistException e){
         return new ErrorResponse(e.getMessage());
     }

     @ExceptionHandler
     @ResponseStatus(HttpStatus.BAD_REQUEST)
     public ErrorResponse handleInvalidEmail(final InvalidEmailException e){
         return new ErrorResponse(e.getMessage());
     }
 */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final IllegalStateException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e){
        return new ErrorResponse("Произошла непредвиденная ошибка");
    }
}

class ErrorResponse {
    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
