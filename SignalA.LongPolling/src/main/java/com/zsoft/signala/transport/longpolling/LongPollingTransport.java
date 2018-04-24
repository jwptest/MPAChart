package com.zsoft.signala.transport.longpolling;

import com.zsoft.signala.ConnectionBase;
import com.zsoft.signala.Transport.ITransport;
import com.zsoft.signala.Transport.StateBase;

public class LongPollingTransport implements ITransport {

	@Override
	public StateBase CreateInitialState(ConnectionBase connection) {
		return new com.zsoft.signala.transport.longpolling.DisconnectedState(connection);
	}

}
