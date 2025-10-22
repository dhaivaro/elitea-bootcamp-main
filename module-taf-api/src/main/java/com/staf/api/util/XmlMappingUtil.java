package com.staf.api.util;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class XmlMappingUtil {

    public static <T> String marshal(final T object, final Class<T> clazz) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            final String xmlString = stringWriter.toString();
            log.info("Marshalled object to XML: {}", xmlString);
            return xmlString;
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to marshal object to XML", e);
        }
    }

    public static <T> T unmarshal(final String xmlString, final Class<T> clazz) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final StringReader stringReader = new StringReader(xmlString);
            return clazz.cast(unmarshaller.unmarshal(stringReader));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to unmarshal XML to object", e);
        }
    }
}
