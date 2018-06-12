package com.example.obdwifi.obd;


import com.example.obdwifi.obdreader.ObdCommand;

public class BusInitObdCommand extends ObdCommand {
	/**
	 * * @param command
	 * */
	public BusInitObdCommand() {
		super("01 00");
	}

	/**
	 * * @param other
	 * */
	public BusInitObdCommand(ObdCommand other) {
		super(other);
	}

	@Override
	public String getFormattedResult() {
		return getResult();
	}

	@Override
	public String getName() {
		return "Bus Init";
	}
}
