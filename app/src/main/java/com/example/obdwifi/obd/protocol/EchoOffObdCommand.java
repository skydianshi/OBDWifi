package com.example.obdwifi.obd.protocol;


import com.example.obdwifi.obdreader.ObdCommand;

/** * This command will turn-off echo. */
public class EchoOffObdCommand extends ObdCommand {
	/**
	 * * @param command
	 * */
	public EchoOffObdCommand() {
		super("AT E0");
	}

	/**
	 * * @param other
	 * */
	public EchoOffObdCommand(ObdCommand other) {
		super(other);
	}

	@Override
	public String getFormattedResult() {
		return getResult();
	}

	@Override
	public String getName() {
		return "Echo Off";
	}
}
