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
package ioio.lib.api;

import ioio.lib.api.exception.ConnectionLostException;

/**
 * Induction control.
 */
public interface Induction extends Closeable {
	public static final short BUTTON_MASK_PLUS_LEFT_FRONT = 0x2000;
	public static final short BUTTON_MASK_PLUS_LEFT_BACK = 0x0800;
	public static final short BUTTON_MASK_PLUS_RIGHT_BACK = 0x1000;
	public static final short BUTTON_MASK_PLUS_RIGHT_FRONT = 0x0040;

	public static final short BUTTON_MASK_MINUS_LEFT_FRONT = (short)0x8000;
	public static final short BUTTON_MASK_MINUS_LEFT_BACK = 0x0008;
	public static final short BUTTON_MASK_MINUS_RIGHT_BACK = 0x0010;
	public static final short BUTTON_MASK_MINUS_RIGHT_FRONT = 0x0400;

	public static final short BUTTON_MASK_POWER_LEFT_FRONT = 0x0080;
	public static final short BUTTON_MASK_POWER_LEFT_BACK = 0x4000;
	public static final short BUTTON_MASK_POWER_RIGHT_BACK = 0x0100;
	public static final short BUTTON_MASK_POWER_RIGHT_FRONT = 0x0004;

	public static final short BUTTON_MASK_CLOCK = 0x0002;
	public static final short BUTTON_MASK_LOCK = 0x0020;
	public static final short BUTTON_MASK_SPARE = 0x0200;
	public static final short BUTTON_MASK_ON_OFF = 0x0001;

	public static final short BUTTON_MASK_POWER_CONTROL_LEFT_FRONT =
			  BUTTON_MASK_PLUS_LEFT_FRONT
			| BUTTON_MASK_MINUS_LEFT_FRONT
			| BUTTON_MASK_POWER_LEFT_FRONT
			;

	public static final short BUTTON_MASK_POWER_CONTROL_LEFT_BACK =
			  BUTTON_MASK_PLUS_LEFT_BACK
			| BUTTON_MASK_MINUS_LEFT_BACK
			| BUTTON_MASK_POWER_LEFT_BACK
			;

	public static final short BUTTON_MASK_POWER_CONTROL_RIGHT_BACK =
			  BUTTON_MASK_PLUS_RIGHT_BACK
			| BUTTON_MASK_MINUS_RIGHT_BACK
			| BUTTON_MASK_POWER_RIGHT_BACK
			;

	public static final short BUTTON_MASK_POWER_CONTROL_RIGHT_FRONT =
			  BUTTON_MASK_PLUS_RIGHT_FRONT
			| BUTTON_MASK_MINUS_RIGHT_FRONT
			| BUTTON_MASK_POWER_RIGHT_FRONT
			;

	public static final short BUTTON_MASK_POWER_CONTROL =
			  BUTTON_MASK_POWER_CONTROL_LEFT_FRONT
			| BUTTON_MASK_POWER_CONTROL_LEFT_BACK
			| BUTTON_MASK_POWER_CONTROL_RIGHT_BACK
			| BUTTON_MASK_POWER_CONTROL_RIGHT_FRONT
			;

	class InductionEvent { }

	class ButtonMaskChangedEvent extends InductionEvent {
		private final short buttonMask;
		private final boolean userPressed;
		public ButtonMaskChangedEvent(short buttonMask, boolean userPressed) {
			this.buttonMask = buttonMask;
			this.userPressed = userPressed;
		}

		public short getButtonMask() {
			return buttonMask;
		}

		public boolean getUserPressed() {
			return userPressed;
		}
	}

	public void setInductionButtonMask(short mask) throws ConnectionLostException;

	public interface EventCallback {
		public void notifyEvent(InductionEvent event);
	}

	public void registerCallback(EventCallback callback);
}
