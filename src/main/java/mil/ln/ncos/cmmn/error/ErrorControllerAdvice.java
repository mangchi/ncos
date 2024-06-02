package mil.ln.ncos.cmmn.error;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import mil.ln.ncos.exception.BizException;

@RestControllerAdvice
public class ErrorControllerAdvice {

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ResponseEntity<ErrorResponse> handleException(Exception e) {
	  e.printStackTrace();
    ErrorResponse response = ErrorResponse.of(ErrorCode.TEMPORARY_SERVER_ERROR);
    response.setDetail(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(value = NoSuchElementException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ResponseEntity<ErrorResponse> handleNoSuchElementException(Exception e) {
	  e.printStackTrace();
    ErrorResponse response = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND);
    response.setDetail(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
  }
  
  //요 밑으로 쭉쭉 추가적인 ExceptionHandler들을 추가해서 처리합니다
  
  /* Biz Error Handler */
  @ExceptionHandler(value = BizException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ResponseEntity<ErrorResponse> handleCustomException(BizException e) {
	  e.printStackTrace();
    ErrorResponse response = ErrorResponse.of(e.getErrorCode());
    response.setDetail(e.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}