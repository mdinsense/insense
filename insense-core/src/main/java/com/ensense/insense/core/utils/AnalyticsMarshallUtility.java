package com.ensense.insense.core.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;


public class AnalyticsMarshallUtility {
	

	public static String doMarshalling(Object obj) throws Exception{
		final StringBuilder resultString = new StringBuilder();
		JAXBContext jaxbContext = JAXBContext
				.newInstance(obj.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(obj, new OutputStream() {			
			@Override
			public void write(int b) throws IOException {
				resultString.append((char) b);			
			}
		});		
		return resultString.toString();	
	}
	
	
}
