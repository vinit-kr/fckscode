package com.ssinfo.ws.rider.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ssinfo.corider.ws.hibernate.HibernateUtil;
import com.ssinfo.ws.rider.mapping.CoriderInfoTbl;
import com.ssinfo.ws.rider.mapping.LocationInfoTbl;
import com.ssinfo.ws.rider.mapping.SearchInfoTbl;
import com.ssinfo.ws.rider.mapping.UserTbl;

public class SearchDao {
	private Logger log = Logger.getLogger(SearchDao.class);

	public SearchDao() {
	}

	private Session mSession = null;
	private Transaction tx = null;

	public SearchInfoTbl persist(SearchInfoTbl transientInstance) {

		log.info("persisting SearchInfoTbl instance");
		mSession = HibernateUtil.getSession();
		try {
			tx = mSession.beginTransaction();
			mSession.persist(transientInstance);
			tx.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re.getCause());
			throw re;
		} finally {
			if (mSession != null) {
				mSession.close();
			}
      	}
		return transientInstance;
	}

	public void remove(SearchInfoTbl persistentInstance) {
		log.debug("removing SearchInfoTbl instance");
		try {
			mSession = HibernateUtil.getSession();
            tx= mSession.beginTransaction();
			mSession.delete(persistentInstance);
			 tx.commit();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
		}finally{
			if(mSession != null){
				mSession.close();
			}
		}
	}

	public SearchInfoTbl merge(SearchInfoTbl detachedInstance) {
		log.debug("merging SearchInfoTbl instance");
		try {
			mSession = HibernateUtil.getSession();
			tx = mSession.beginTransaction();
			SearchInfoTbl result = (SearchInfoTbl) mSession
					.merge(detachedInstance);
			log.debug("merge successful");
			tx.commit();
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}finally{
			if(mSession != null){
				mSession.close();
			}
		}
	}

	public SearchInfoTbl findById(long id) {
		log.debug("getting SearchInfoTbl instance with id: " + id);
		SearchInfoTbl searchInstance = null;
		try {
			mSession = HibernateUtil.getSession();
			tx = mSession.beginTransaction();
			searchInstance = (SearchInfoTbl) mSession.get(
					SearchInfoTbl.class, id);
			tx.commit();
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			
		}finally{
			if(mSession != null){
				mSession.close();
			}
		}
		return searchInstance;
	}
	
	public List<UserTbl> findAllAvailableCoriders(LocationInfoTbl currentLocation,long userId) {
		log.info("findAllAvailableCoriders start"+Calendar.getInstance().getTime());
		//for avoiding nullpointer exception.
		List<UserTbl> userObjList = new ArrayList<UserTbl>();;
		try {
			
			mSession = HibernateUtil.getSession();
			Calendar currentTime = Calendar.getInstance();
			currentTime.add(Calendar.MINUTE, -15);
			Date currentDate = new Date(currentTime.getTimeInMillis());
			String availableUser = "select search.userTbl.userId as userId from SearchInfoTbl as search,"
				+ "LocationInfoTbl as location where search.userTbl.userId !=:currentUserId and search.searchDate >=:currentTime and "
					+ " search.locationInfoTblByUserCurrentLocationInfoId in (select locationIn.locationId from LocationInfoTbl locationIn where "
					+ "SQRT(POW((locationIn.latitude) - :currentLat , 2) + POW((locationIn.longitude) - :currentLong, 2)) * 100 < 2000) "
					+ "GROUP BY search.userTbl.userId";
			mSession = HibernateUtil.getSession();
			Query query = mSession.createQuery(availableUser);
			
			query.setParameter("currentTime", currentDate);
			query.setParameter("currentUserId", userId);
			query.setParameter("currentLat", currentLocation.getLatitude());
			query.setParameter("currentLong", currentLocation.getLongitude());
			List<Long> userList = query.list();
			if(userList.size()>0){
				// Available users
				Query allUserQueries = mSession.createQuery(
						"from UserTbl user where  user.userId in (:ids)")
						.setParameterList("ids", userList);
				userObjList = allUserQueries.list();
				log.info("user list is "+userObjList.size());
			}
			
		} catch (HibernateException he) {
			log.info(he.getStackTrace());
			
		}finally{
			if(mSession !=null){
				mSession.close();
			}
		}
		//log.info("findAllAvailableCoriders end"+Calendar.getInstance().getTime());
		return userObjList;
	}
	
	public List<CoriderInfoTbl> saveAllAvailableCoriders(List<CoriderInfoTbl> coridersList){
		try{
			mSession = HibernateUtil.getSession();
			tx = mSession.beginTransaction();
			for(CoriderInfoTbl corider:coridersList){
				mSession.persist(corider);
			}
			tx.commit();
			
		}catch(HibernateException he){
			log.info(he.getMessage());
			tx.rollback();
		}finally{
			if(mSession !=null){
				mSession.close();
			}
		}
		return coridersList;
	}
	
	public void updateCoriderStatus(CoriderInfoTbl corider){
		try{
		mSession = HibernateUtil.getSession();
		tx = mSession.beginTransaction();
		mSession.update(corider);
		tx.commit();
		}catch(HibernateException he){
			log.info(he.getMessage());
		}finally{
			if(mSession !=null){
				mSession.close();
			}
		}
	}
	
	public void updateSearchInfo(SearchInfoTbl searchInfo){
		try{
			mSession = HibernateUtil.getSession();
			tx = mSession.beginTransaction();
			mSession.update(searchInfo);
			tx.commit();
			}catch(HibernateException he){
				log.info(he.getMessage());
			}finally{
				if(mSession !=null){
					mSession.close();
				}
			}
	}
	
	public SearchInfoTbl getLatestSearchInfo(long userId){
		SearchInfoTbl searchInfo = null;
		try{
			mSession = HibernateUtil.getSession();
			tx = mSession.beginTransaction();
			Query query = mSession.createQuery("from SearchInfoTbl as search where search.userTbl.userId=:userId order by search.searchId desc");
			query.setMaxResults(1);
			query.setParameter("userId", userId);
			List<SearchInfoTbl> searchList = query.list();
			if(searchList.size() >0){
				searchInfo = searchList.get(0);
			}
			tx.commit();
		}catch(HibernateException he){
			log.error(he.getMessage());
		}finally {
			if(mSession !=null){
				mSession.close();
			}
		}
		return searchInfo;
	}
	
}
