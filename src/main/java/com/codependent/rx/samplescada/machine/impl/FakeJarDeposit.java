package com.codependent.rx.samplescada.machine.impl;

import com.codependent.rx.samplescada.machine.JarDeposit;

public class FakeJarDeposit extends JarDeposit{

	public FakeJarDeposit(String id, int capacity) {
		super(id, capacity);
	}

	@Override
	protected void doDropJar() {
		
	}

}
