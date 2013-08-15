package ioio.lib.impl;

import ioio.lib.api.TemperatureSensor;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.impl.IncomingState.TemperatureSensorListener;

import java.util.LinkedList;

class TemperatureSensorImpl extends AbstractResource implements
		TemperatureSensor, TemperatureSensorListener {
	private final LinkedList<EventCallback> callbacks = new LinkedList<EventCallback>();

	TemperatureSensorImpl(IOIOImpl ioio) throws ConnectionLostException {
		super(ioio);
	}

	@Override
	public synchronized void disconnected() {
		super.disconnected();
	}

	@Override
	public synchronized void reportTemperatureSensorData(long temperatureSensorBits) {
		TemperatureDataEvent temperatureEvent = new TemperatureDataEvent(temperatureSensorBits);
		for (EventCallback callback : callbacks) {
			callback.notifyEvent(temperatureEvent);
		}
	}

	@Override
	public void registerCallback(EventCallback callback) {
		callbacks.add(callback);
	}
}
