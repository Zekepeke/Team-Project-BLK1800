package src.Server;

public enum ServerState {
	SEND_HANDSHAKE,
	READ_HANDSHAKE,
	READ_DATA,
	EXECUTE
}
