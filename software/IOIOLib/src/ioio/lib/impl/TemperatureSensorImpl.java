package ioio.lib.impl;

import ioio.lib.api.TemperatureSensor;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.impl.IncomingState.TemperatureSensorListener;

import java.util.LinkedList;

class TemperatureSensorImpl extends AbstractResource implements
		TemperatureSensor, TemperatureSensorListener {
	private final LinkedList<TemperatureEvent> events = new LinkedList<TemperatureEvent>();

	TemperatureSensorImpl(IOIOImpl ioio) throws ConnectionLostException {
		super(ioio);
	}

	@Override
	public synchronized int getEventCount() {
		return events.size();
	}

	@Override
	public synchronized TemperatureEvent readEvent() throws ConnectionLostException, InterruptedException {
		while ((state_ == State.OPEN) && (events.size() == 0)) {
			checkState();
			wait();
		}
		return events.remove();
	}

	@Override
	public synchronized void disconnected() {
		super.disconnected();
	}

	@Override
	public synchronized void reportTemperatureSensorData(long temperatureSensorBits) {
		events.addLast(new TemperatureDataEvent(temperatureSensorBits));
		notifyAll();
	}
}
