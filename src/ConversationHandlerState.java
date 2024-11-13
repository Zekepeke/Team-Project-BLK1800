package src;

public enum ConversationHandlerState {
	SEND_HANDSHAKE,
	READ_HANDSHAKE,
	READ_DATA,
	EXECUTE
}
