package com.ssinfo.corider.ws.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {

	
	private static SessionFactory sessionFactory;
    static {
        try {
        	// Create the SessionFactory form hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            System.out.println("config properties "+configuration.getProperties());
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
          
        } catch (Throwable ex) {
            // Log exception!
        	 System.out.println("Initial SessionFactory creation failed." + ex);
        	ex.printStackTrace();
        	
           
        }
       
    }

    public static Session getSession()
            throws HibernateException {
    	if(sessionFactory !=null){
    		return sessionFactory.openSession();
    	}else {
    		throw new HibernateException("Session factory is null");
    	}
         
    }
}
