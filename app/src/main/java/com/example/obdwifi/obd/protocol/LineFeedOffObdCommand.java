package com.example.obdwifi.obd.protocol;


import com.example.obdwifi.obdreader.ObdCommand;

/** * Turns off line-feed. */
public class LineFeedOffObdCommand extends ObdCommand {
	/**
	 * * @param command
	 * */
	public LineFeedOffObdCommand() {
		super("AT L0");
	}

	/**
	 * * @param other
	 * */
	public LineFeedOffObdCommand(ObdCommand other) {
		super(other);
	}

	@Override
	public String getFormattedResult() {
		return getResult();
	}

	@Override
	public String getName() {
		return "Line Feed Off";
	}
}
