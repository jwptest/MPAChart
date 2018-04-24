package com.zsoft.signala.Transport;

import com.zsoft.signala.ConnectionBase;

public interface ITransport {
	StateBase CreateInitialState(ConnectionBase connectionBase);
}
