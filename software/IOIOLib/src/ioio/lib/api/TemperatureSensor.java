package ioio.lib.api;

/**
 * Temperature sensor interface.
 */
public interface TemperatureSensor extends Closeable {

	// Lets have some sanity values
	static final int MAX_VALID_TEMPERATURE_IN_CELSIUS = 250;
	static final int MIN_VALID_TEMPERATURE_IN_CELSIUS = -50;

	class TemperatureEvent { }

	class TemperatureDataEvent extends TemperatureEvent {
		private final long temperatureSensorData;
		public TemperatureDataEvent(long temperatureSensorData) {
			this.temperatureSensorData = temperatureSensorData;
			//System.out.print("Raw packet:");
			//printBinaryString((byte) ((temperatureSensorData >> 28) & 0xFF), 8);
			//printBinaryString((byte) ((temperatureSensorData >> 20) & 0xFF), 8);
			//printBinaryString((byte) ((temperatureSensorData >> 12) & 0xFF), 8);
			//printBinaryString((byte) ((temperatureSensorData >> 4) & 0xFF), 8);
			//printBinaryString((byte) (temperatureSensorData & 0x0F), 4);
			//System.out.println();
		}

		/* The Rubiscon send the data as:
		 * %00111100 00000000 01110011 11011011 0001
		 *  tttttttt tttt              aaaaaaaa
		 *  32107654 BA98
		 *
		 *  where t=temperature data, and a seems to change every power cycle
		 *  The nibbles are swapped and temperature is stored in fahrenheit + 90
		 *
		 *  If the temperature is invalid (sensor removed) the packet looks like this:
		 * %11001100 11000000 00101100 11101101 0001
		 */
		/**
		 * @return The temperature in Fahrenheit from the Rubiscon barbecue thermometer.
		 */
		public int getTemperatureInFahrenheit() {
			int tempInFahrenheit = nibbleSwap((byte) ((temperatureSensorData >> 28) & 0xFF)) & 0xFF;
			int tempHighBits = (int) ((temperatureSensorData >> 24) & 0x0F);
			tempInFahrenheit |= tempHighBits << 8;
			tempInFahrenheit = tempInFahrenheit - 90;
			return tempInFahrenheit;
		}

		/**
		 * @return The temperature in Celsius from the Rubiscon barbecue thermometer.
		 */
		public int getTemperatureInCelsius() {
			int fahrenheit = getTemperatureInFahrenheit();
			return ((fahrenheit - 32) * 5) / 9;
		}

		public boolean isValid() {
			int celsius = getTemperatureInCelsius();
			return (celsius <= MAX_VALID_TEMPERATURE_IN_CELSIUS) &&
					(celsius >= MIN_VALID_TEMPERATURE_IN_CELSIUS);
		}

		/**
		 * @return The address of the thermometer that sent this event.
		 */
		public byte getAddress() {
			// Lets assume that also the address byte is nibble swapped. It does not really matter.
			byte address = nibbleSwap((byte) ((temperatureSensorData >> 4) & 0xFF));
			return address;
		}

		private static byte nibbleSwap(byte in) {
			byte out = 0;
			byte upperNibble = (byte) ((in >> 4) & 0x0f);
			byte lowerNibble = (byte) (in & 0x0f);
			out |= upperNibble;
			out |= (byte) (lowerNibble << 4);
			return out;
		}

		//private static void printBinaryString(byte binary, int digits) {
		//	for (int i = (8 - digits); i < 8; i++) {
		//		if ((binary & (1 << (7 - i))) != 0) {
		//			System.out.print("1");
		//		} else {
		//			System.out.print("0");
		//		}
		//	}
		//	System.out.print(" ");
		//}
	}

	public interface EventCallback {
		public void notifyEvent(TemperatureEvent temperatureEvent);
	}

	public void registerCallback(EventCallback callback);
}
