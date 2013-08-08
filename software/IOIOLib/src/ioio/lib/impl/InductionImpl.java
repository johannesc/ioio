/*
 * Copyright 2011 Ytai Ben-Tsvi. All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ARSHAN POURSOHI OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
 */
package ioio.lib.impl;

import ioio.lib.api.Induction;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.impl.IncomingState.InductionListener;

import java.io.IOException;
import java.util.LinkedList;

class InductionImpl extends AbstractResource implements Induction, InductionListener {
	private enum State {
		OPEN, CLOSED
	};

	private final State state = State.OPEN;

	private final LinkedList<InductionEvent> events = new LinkedList<InductionEvent>();
	private final LinkedList<EventCallback> callbacks = new LinkedList<EventCallback>();

	InductionImpl(IOIOImpl ioio) throws ConnectionLostException {
		super(ioio);
	}

	@Override
	synchronized public void setInductionButtonMask(short mask) throws ConnectionLostException {
		try {
			ioio_.protocol_.setInductionButtonMask(mask);
		} catch (IOException e) {
			throw new ConnectionLostException(e);
		}
	}

	@Override
	public synchronized void reportButtonMask(short buttonMask, boolean userPressed) {
		ButtonMaskChangedEvent event = new ButtonMaskChangedEvent(buttonMask, userPressed);
		events.addLast(event);
		notifyAll();
		for (EventCallback callback : callbacks) {
			callback.notifyEvent(event);
		}
	}

	@Override
	public int getEventCount() {
		synchronized (this) {
			return events.size();
		}
	}

	@Override
	public synchronized InductionEvent readEvent() throws ConnectionLostException, InterruptedException {
		while ((state == State.OPEN) && (events.size() == 0)) {
			wait();
		}
		if (state != State.OPEN) {
			throw new ConnectionLostException();
		}
		return events.remove();
	}

	@Override
	public synchronized void disconnected() {
		super.disconnected();
	}

	@Override
	public void registerCallback(EventCallback callback) {
		callbacks.add(callback);
	}
}
