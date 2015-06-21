package cz.muni.expense.wb;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import cz.muni.expense.data.MessageRepository;
import cz.muni.expense.model.Message;


@ServerEndpoint("/websocket")
public class WebSocketTest {
	
	@Inject
	private MessageRepository repository;
	
	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		System.out.println("User input: " + message + " !");
		
		repository.create(getMessageFromJson(message));
	}

	@OnOpen
	public void onOpen() {
		System.out.println("Client connected");
	}

	@OnClose
	public void onClose() {
		System.out.println("Connection closed");
	}
	
	public Message getMessageFromJson(String jsonContent) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(jsonContent);
        jp.nextToken();

        Message message = new Message();
        while (jp.hasCurrentToken()) {
            JsonToken currentToken = jp.getCurrentToken();
            if (currentToken == JsonToken.START_OBJECT) {

                message = mapper.readValue(jp, Message.class);
            }
            jp.nextToken();
        }

        return message;
    }
}
