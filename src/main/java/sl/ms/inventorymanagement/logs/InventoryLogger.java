package sl.ms.inventorymanagement.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringMapMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class InventoryLogger extends StringMapMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER= LogManager.getLogger("sl.ms.invent.logger");
	private static final String TRANS_ID="TRANS_ID";
	private static final String REQUEST="REQUEST";
	private static final String START_TIME="START_TIME";
	private static final String END_TIME="END_TIME";
	
	private void logMessage(String key, String value) {
		if (key!=null && value!=null) {
			put(key,value);
		}
	}
	
	public void addInventoryLogs(String startTime, String endTime,Object request) {
		ObjectMapper mapper=new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		logMessage(START_TIME,startTime);
		logMessage(END_TIME,endTime);
		try {
			logMessage(REQUEST,mapper.writeValueAsString(request));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		LOGGER.info("cloud sleuth testing");
		LOGGER.info(getData());
	}
	
	public void errorLogs(Object errorObject,String statusCode) {
		ObjectMapper mapper=new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		logMessage("HttpStatus",statusCode);
		try {
			logMessage("ERROR_MESSAGE",mapper.writeValueAsString(errorObject));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		LOGGER.info(getData());
	}
}
