package com.algomized.springframework.http.converter.json;

import java.io.IOException;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.algomized.springframework.web.servlet.handler.ViewThread;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Extracts {@link Views} filter from a {@link ViewThread} object and 
 * serialize objects using the filter.
 * 
 * @author Poh Kah Kong
 *
 */
public class JsonViewAwareMessageConverter extends MappingJackson2HttpMessageConverter {
	private ObjectMapper objectMapper;
	private String jsonPrefix;
	
	public JsonViewAwareMessageConverter() {
		super();
		objectMapper = getObjectMapper();
		objectMapper.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
	}

	/**
	 * Extracts {@link Views} filter from a {@link ViewThread} object and uses 
	 * Jackson ObjectMapper to write objects with the view filter.
	 * 
	 */
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {		
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		@SuppressWarnings("deprecation")
		JsonGenerator jsonGenerator =
				objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);

		// A workaround for JsonGenerators not applying serialization features
		// https://github.com/FasterXML/jackson-databind/issues/12
		if (objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			jsonGenerator.useDefaultPrettyPrinter();
		}
		
		ObjectWriter writer = null;
		
        Class<?>[] jsonViews = ViewThread.getKey();  

        if (jsonViews != null) { 
        	writer = objectMapper.writerWithView(jsonViews[0]); 
        } else { 
        	writer = objectMapper.writer(); 
        } 		

		try {
			if (this.jsonPrefix != null) {
				jsonGenerator.writeRaw(this.jsonPrefix);
			}
			writer.writeValue(jsonGenerator, object);
		}		
		catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	public void setJsonPrefix(String jsonPrefix) {
		this.jsonPrefix = jsonPrefix;
	}

	@Override
	public void setPrefixJson(boolean prefixJson) {
		this.jsonPrefix = (prefixJson ? "{} && " : null);
	}
}