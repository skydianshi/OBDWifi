package com.example.obdwifi.obd.protocol;


import com.example.obdwifi.obd.ObdProtocols;
import com.example.obdwifi.obdreader.ObdCommand;

/** * Select the protocol to use. */
public class SelectProtocolObdCommand extends ObdCommand {
	private final ObdProtocols protocol;

	/**
	 * * @param command
	 * */
	public SelectProtocolObdCommand(ObdProtocols protocol) {
		super("AT SP " + protocol.getValue());
		this.protocol = protocol;
	}

	@Override
	public String getFormattedResult() {
		return getResult();
	}

	@Override
	public String getName() {
		return "Select Protocol " + protocol.name();
	}
}
