package com.ssinfo.corider.ws.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CoriderPropertyInitializer implements ServletContextListener{
	private Properties mProperties;
	private static final String PATH = "corider.properties";
	private final Logger logger = Logger.getLogger(getClass().getName());
	@Override
	public void contextDestroyed(ServletContextEvent servletContext) {
		this.mProperties = null;
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		this.mProperties = new Properties();
		InputStream inputStream  = CoriderPropertyInitializer.class.getClassLoader().getResourceAsStream(PATH);
		try {
			this.mProperties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key){
		return mProperties.getProperty(key);
	}
	
}
