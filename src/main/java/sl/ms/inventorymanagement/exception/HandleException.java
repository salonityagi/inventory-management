package sl.ms.inventorymanagement.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import sl.ms.inventorymanagement.logs.InventoryLogger;

@ControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {
	InventoryLogger logger=new InventoryLogger();
	
	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage error=new ErrorMessage();
		error.setErrorCode("E100");
		error.setErrorDetail(ex.getLocalizedMessage());
		error.setHttpStatus(status);
		logger.errorLogs(error, status.name());
		return new ResponseEntity<>(error,status);
	}
	
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleAllExceptions(
			Exception ex, WebRequest request) {
		ErrorMessage error=new ErrorMessage();
		error.setErrorCode("E101");
		error.setErrorDetail(ex.getLocalizedMessage());
		error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		logger.errorLogs(error, HttpStatus.INTERNAL_SERVER_ERROR.name());
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value= {ProductNotFound.class})
	public ResponseEntity<Object> notFoundExceptions(
			Exception ex, WebRequest request) {
		ErrorMessage error=new ErrorMessage();
		error.setErrorCode("E102");
		error.setErrorDetail(ex.getLocalizedMessage());
		error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		logger.errorLogs(error, HttpStatus.INTERNAL_SERVER_ERROR.name());
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
