package cz.muni.expense.wb;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
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

import cz.muni.expense.auth.AuthService;
import cz.muni.expense.data.MessageRepository;
import cz.muni.expense.data.UserRepository;
import cz.muni.expense.enums.UserRole;
import cz.muni.expense.model.Message;
import cz.muni.expense.model.User;


@ServerEndpoint("/websocket")
public class WebSocketMessager {
	
	private final Set<String> allowedRoles = new HashSet<>(Arrays.asList(UserRole.ADMIN.toString(), UserRole.PRIVILEGED_USER.toString()));
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private MessageRepository messageRepository;
	
	@Inject
	private AuthService authService;
	
	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		System.out.println("User input: " + message);
		
		WebSocketMessage wsm = getWebSocketMessageFromJson(message);
		User user = userRepository.findById(wsm.getUserId());

		boolean isAuthorized = authService.isAuthorized(user.getName(), wsm.getToken(), allowedRoles);		
		if(isAuthorized){
			Message msg = new Message();
			msg.setTimestamp(wsm.getTimestamp());
			msg.setUser(user);
			msg.setAction(wsm.getAction());
			messageRepository.create(msg);
		} else {
			System.out.println("not authorized");
		}
	}

	@OnOpen
	public void onOpen() throws IOException {		
		System.out.println("Client connected");
	}

	@OnClose
	public void onClose() throws IOException {
		System.out.println("Connection closed");
	}
	
	private WebSocketMessage getWebSocketMessageFromJson(String jsonContent) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(jsonContent);
        jp.nextToken();

        WebSocketMessage message = new WebSocketMessage();
        while (jp.hasCurrentToken()) {
            JsonToken currentToken = jp.getCurrentToken();
            if (currentToken == JsonToken.START_OBJECT) {

                message = mapper.readValue(jp, WebSocketMessage.class);
            }
            jp.nextToken();
        }

        return message;
    }
}
