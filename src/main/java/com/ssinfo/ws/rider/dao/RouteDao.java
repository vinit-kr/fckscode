package com.ssinfo.ws.rider.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ssinfo.corider.ws.hibernate.HibernateUtil;
import com.ssinfo.ws.rider.mapping.LegsTbl;
import com.ssinfo.ws.rider.mapping.RoutesTbl;
import com.ssinfo.ws.rider.mapping.StepsTbl;

public class RouteDao {
 private Session mSession = null;
 private Transaction tx = null;
	public RouteDao() {
		// TODO Auto-generated constructor stub
	}
	
	
	public RoutesTbl saveRoutesInfo(RoutesTbl routesTbl){
		
		mSession = HibernateUtil.getSession();
		try{
			tx = mSession.beginTransaction();
			mSession.persist(routesTbl);
			tx.commit();
		}catch(HibernateException he){
			he.printStackTrace();
		}finally {
			if(mSession !=null){
				mSession.close();
			}
		}
		
		return routesTbl;
	}
	
	public LegsTbl saveLegsInfo(LegsTbl legsTbl){
		mSession = HibernateUtil.getSession();
		try{
			tx = mSession.beginTransaction();
			mSession.persist(legsTbl);
			tx.commit();
		}catch(HibernateException he){
			he.printStackTrace();
		}finally {
			if(mSession !=null){
				mSession.close();
			}
			
		}
		return legsTbl;
	}
	
	public StepsTbl saveSteps(StepsTbl stepsInfo){
		mSession = HibernateUtil.getSession();
		try{
			tx = mSession.beginTransaction();
			mSession.persist(stepsInfo);
			tx.commit();
		}catch(HibernateException he){
			he.printStackTrace();
		}finally {
			if(mSession !=null){
				mSession.close();
			}
			
		}
		return stepsInfo;
	}

}
