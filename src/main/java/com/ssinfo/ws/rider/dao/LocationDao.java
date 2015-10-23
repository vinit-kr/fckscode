package com.ssinfo.ws.rider.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ssinfo.corider.ws.hibernate.HibernateUtil;
import com.ssinfo.ws.rider.mapping.LocationInfoTbl;

public class LocationDao {
	private Logger log = Logger.getLogger(SearchDao.class);
	public LocationDao() {
	}
		private Session mSession = null;
		private Transaction tx = null;

		public void persist(LocationInfoTbl transientInstance) {
			log.debug("persisting LocationInfoTbl instance");
			mSession = HibernateUtil.getSession();
			try {
				tx = mSession.beginTransaction();
				mSession.persist(transientInstance);
				tx.commit();
				log.debug("persist successful");
			} catch (RuntimeException re) {
				log.error("persist failed", re);
				mSession.flush();
				tx.rollback();
				throw re;
			}finally{
				if(mSession !=null){
					mSession.close();
				}
			}
		}

		public void remove(LocationInfoTbl persistentInstance) {
			log.debug("removing LocationInfoTbl instance");
			mSession = HibernateUtil.getSession();
			try {
				tx = mSession.beginTransaction();
				mSession.delete(persistentInstance);
				tx.commit();
				log.debug("remove successful");
			} catch (RuntimeException re) {
				log.error("remove failed", re);
				mSession.flush();
				tx.rollback();
				throw re;
			}finally{
				if(mSession !=null){
					mSession.close();
				}
			}
		}

		public LocationInfoTbl merge(LocationInfoTbl detachedInstance) {
			log.debug("merging LocationInfoTbl instance");
			mSession = HibernateUtil.getSession();
			try {
				tx = mSession.beginTransaction();
				LocationInfoTbl result = (LocationInfoTbl) mSession.merge(detachedInstance);
				tx.commit();
				log.debug("merge successful");
				return result;
			} catch (RuntimeException re) {
				log.error("merge failed", re);
				throw re;
			}finally{
				if(mSession !=null){
					mSession.close();
				}
			}
		}

		public LocationInfoTbl findById(long id) {
			log.debug("getting LocationInfoTbl instance with id: " + id);
			mSession = HibernateUtil.getSession();
			try {
				tx = mSession.beginTransaction();
				LocationInfoTbl instance = (LocationInfoTbl) mSession.get(
						LocationInfoTbl.class, id);
				tx.commit();
				log.debug("get successful");
				return instance;
			} catch (RuntimeException re) {
				log.error("get failed", re);
				throw re;
			}finally{
				if(mSession !=null){
					mSession.close();
				}
			}
		}

}
