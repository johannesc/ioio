package ioio.examples.hello_console;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Induction;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOConnectionManager.Thread;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.pc.IOIOConsoleApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelloIOIOConsole extends IOIOConsoleApp {
	private boolean ledOn_ = false;
	protected byte rightMask = 0x00;
	protected byte leftMask = 0x00;

	// Boilerplate main(). Copy-paste this code into any IOIOapplication.
	public static void main(String[] args) throws Exception {
		new HelloIOIOConsole().go(args);
	}

	@Override
	protected void run(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		boolean abort = false;
		String line;
		while (!abort && (line = reader.readLine()) != null) {
			if (line.equals("t")) {
				ledOn_ = !ledOn_;
			} else if (line.equals("n")) {
				ledOn_ = true;
			} else if (line.equals("f")) {
				ledOn_ = false;
			} else if (line.equals("q")) {
				abort = true;
			} else if (line.equals("s")) {
				rightMask = (byte)( (rightMask << 1) & 0xFF);
				leftMask = (byte)( (leftMask << 1) & 0xFF);
				System.out.println(String.format("Right mask is now 0x%x", rightMask));
				System.out.println(String.format("Left mask is now 0x%x", leftMask));
			} else {
				try {
					int mask = Integer.parseInt(line, 16);
					rightMask = (byte)( mask & 0xFF);
					leftMask = (byte)( mask & 0xFF);
					System.out.println(String.format("Right mask is now 0x%x", rightMask));
					System.out.println(String.format("Left mask is now 0x%x", leftMask));
				} catch(NumberFormatException e) {
					System.out
					.println("Unknown input. t=toggle, n=on, f=off, s=shift, XX = set mask, q=quit.");
				}
			}
		}
	}

	@Override
	public IOIOLooper createIOIOLooper(String connectionType, Object extra) {
		return new BaseIOIOLooper() {
			private DigitalOutput led_;
			private Induction induction;
			private byte lastLeftMask = 0;
			private byte lastRightMask = 0;
			private boolean lastLedOn;

			@Override
			protected void setup() throws ConnectionLostException,
					InterruptedException {
				led_ = ioio_.openDigitalOutput(IOIO.LED_PIN, true);
				induction = ioio_.openInduction();
			}

			@Override
			public void loop() throws ConnectionLostException,
					InterruptedException {
				if (lastLedOn != ledOn_) {
					led_.write(!ledOn_);
					lastLedOn = ledOn_;
				}
				if (lastLeftMask != leftMask || lastRightMask != rightMask) {
					induction.setInductionButtonMask(leftMask, rightMask);
					lastLeftMask = leftMask;
					lastRightMask = rightMask;
				}
				Thread.sleep(10);
			}
		};
	}
}
