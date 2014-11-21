package com.highgo.hgdbadmin.log;

import com.highgo.hgdbadmin.vthread.Record;

public class Pair {
	public Long id;
	public Record record;

	public Pair(Long id, Record record) {
		super();
		this.id = id;
		this.record = record;
	}

	public Record get() {
		return record;
	}

	@Override
	public String toString() {
		return "id:" + this.id + "=record:" + this.record;
	}
}
