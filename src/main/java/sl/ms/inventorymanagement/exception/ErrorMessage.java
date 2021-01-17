package sl.ms.inventorymanagement.exception;

public class ErrorMessage {

	private String errorMessage;
	private String errorCode;
	private String errorDetail;
	private org.springframework.http.HttpStatus httpStatus;
	private String path;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public org.springframework.http.HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(org.springframework.http.HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
