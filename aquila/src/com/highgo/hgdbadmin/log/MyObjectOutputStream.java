package com.highgo.hgdbadmin.log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream {

	public MyObjectOutputStream(OutputStream os) throws IOException, SecurityException {
		super(os);
	}

	@Override
	protected void writeStreamHeader() throws IOException {
		super.reset();
	}
}
