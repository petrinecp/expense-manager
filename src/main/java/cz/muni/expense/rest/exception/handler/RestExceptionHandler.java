package cz.muni.expense.rest.exception.handler;

import java.util.Iterator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionHandler implements
		ExceptionMapper<Throwable> {

	/**
	 * Intercepts exceptions and looking for ConstraintViolationException. ConstraintViolationException will be returned to client as json.
	 * @param exception
	 * @return ValidationErrorDto as json.
	 */
	@Override
	public Response toResponse(Throwable exception) {
		ConstraintViolationException violationException = getConstraintViolationException(exception);
		ValidationErrorDto validationErrorDto = new ValidationErrorDto();
		
		if (violationException != null){
			Iterator<ConstraintViolation<?>> iterator = violationException.getConstraintViolations().iterator();
			
			while (iterator.hasNext()){
				ConstraintViolation<?> violation = iterator.next();
				String field = violation.getPropertyPath().toString();
				String message = violation.getMessage();
				validationErrorDto.addFieldError(field, message);
			}
			return Response.status(Status.BAD_REQUEST)
					.entity(validationErrorDto)
					.type(MediaType.APPLICATION_JSON).build();
		} else {
			validationErrorDto.addFieldError("InternalServerError", exception.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(validationErrorDto)
					.type(MediaType.APPLICATION_JSON).build();
		}
	}
	
	/**
	 * Goes recursively through exception causes and return ConstraintViolationException of null.
	 * @param exception
	 * @return ConstraintViolationException or null
	 */
	private ConstraintViolationException getConstraintViolationException(Throwable exception){
		Throwable exceptionCause = exception.getCause();
		if (exceptionCause != null ){
			if (exceptionCause instanceof ConstraintViolationException){
				return (ConstraintViolationException) exceptionCause;
			} else {
				return this.getConstraintViolationException(exceptionCause);
			}
		} else {
			return null;
		}
	}
}
