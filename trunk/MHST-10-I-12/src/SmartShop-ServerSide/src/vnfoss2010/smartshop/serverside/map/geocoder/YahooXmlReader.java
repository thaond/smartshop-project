package vnfoss2010.smartshop.serverside.map.geocoder;

import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;


public class YahooXmlReader {
	private YahooXmlReader() {
		// no instantiation
	}

	public static Point readConfig(InputStream in) {
		Point coordinates = new Point();
		try {
			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			// InputStream in = new FileInputStream(configFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// Read the XML document
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					if (event.asStartElement().getName().getLocalPart() == ("Latitude")) {
						event = eventReader.nextEvent();
						coordinates.setLat(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}
					if (event.asStartElement().getName().getLocalPart() == ("Longitude")) {
						event = eventReader.nextEvent();
						coordinates
								.setLng(Double.parseDouble(event.asCharacters().getData()));
						continue;
					}
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return coordinates;
	}

}
