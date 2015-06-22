package cz.muni.expense.configuration;

import java.text.SimpleDateFormat;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;


/**
 *
 * @author Lukáš Valach
 */
@Provider  
@Produces(MediaType.APPLICATION_JSON)  
public class JacksonConfig implements ContextResolver<ObjectMapper>  
{  
   private ObjectMapper objectMapper;  
  
  
   public JacksonConfig() throws Exception  
   {  
      this.objectMapper = new ObjectMapper();  
      this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);  
   }  
  	
  
   public ObjectMapper getContext(Class<?> objectType)  
   {  
      return objectMapper;  
   }  
}  
