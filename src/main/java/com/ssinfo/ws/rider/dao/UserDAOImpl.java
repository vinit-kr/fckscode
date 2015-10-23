package com.ssinfo.ws.rider.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ssinfo.corider.ws.hibernate.HibernateUtil;
import com.ssinfo.corider.ws.model.DeviceInfo;
import com.ssinfo.corider.ws.model.User;
import com.ssinfo.corider.ws.util.ServiceHelper;
import com.ssinfo.ws.rider.mapping.CoriderInfoTbl;
import com.ssinfo.ws.rider.mapping.DeviceInfoTbl;
import com.ssinfo.ws.rider.mapping.RoutesTbl;
import com.ssinfo.ws.rider.mapping.UserTbl;

public class UserDAOImpl extends Dao implements IUserDAO {
	static Logger logger = Logger.getLogger(UserDAOImpl.class);
	private Session mSession = null;
	private Transaction transaction = null;

	@Override
	public UserTbl saveUserInfo(User userInfo) {
		logger.info("Inside saveUserInfo");

		UserTbl user = new UserTbl();
		user.setUserName(userInfo.getUserName());
		user.setRegistrationDate(new Date());
		Calendar calendar = Calendar.getInstance();
        String gcmRegId = userInfo.getGcmRegistrationId();
        user.setGcmId(gcmRegId);
		user.setRegistrationTime(calendar.getTime());
		user.setEmailId(userInfo.getEmailId());
		user.setStatus(ServiceHelper.generateVerificationCode());
		// Device info
		DeviceInfoTbl device = new DeviceInfoTbl();
		DeviceInfo deviceInfo = userInfo.getDevice();
		device.setDeviceOsType(deviceInfo.getOsType());
		device.setDeviceModelNo(deviceInfo.getModelNo());
		device.setAppVersion(deviceInfo.getAppVersion());
		device.setDeviceOsVersion(deviceInfo.getOsVersion());
		device.setDeviceScreenResolution(deviceInfo.getScreenResolution());
		device.setDeviceScreenSize(Double.valueOf(deviceInfo.getScreenSize()));
		device.setUserTbl(user);
		// save device info
		Set<DeviceInfoTbl> deviceSet = new HashSet<DeviceInfoTbl>();
		deviceSet.add(device);
		user.setDeviceInfoTbls(deviceSet);
		try {
			mSession = HibernateUtil.getSession();
			if (mSession != null) {
				transaction = mSession.beginTransaction();
				mSession.persist(user);
				transaction.commit();
				// session.close();
			}

		} catch (HibernateException he) {
			logger.info(he.getMessage());
			throw he;

		} finally {
			if (mSession != null) {
				mSession.close();
			}
		}
		return user;
	}

	@Override
	public void remove(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUserInfo(User userInfo) {
		// TODO Auto-generated method stub

	}

	public int updateCoriderStatus(long coriderId,long userId ){
          String query = "from CoriderInfoTbl corider where corider.userTbl.userId =:matchedCorider and corider.searchInfoTbl.searchId =:searchId";
		
		try {
			mSession = HibernateUtil.getSession();
			transaction = mSession.beginTransaction();
			Query queryObj = mSession.createQuery(query);
			queryObj.setParameter("matchedCorider", coriderId);
			queryObj.setParameter("searchId", userId);
			return queryObj.executeUpdate();
		} catch (HibernateException he) {
			logger.error(he.getMessage());
		} finally {
			if (mSession != null) {
				mSession.close();
			}
		}
		logger.info("findById end"+Calendar.getInstance().getTime());
		
	
		
		return 0;
	}
	
	public UserTbl findById(long userId) {
		logger.info("findById start"+Calendar.getInstance().getTime());
		UserTbl user = null;
		try {
			mSession = HibernateUtil.getSession();
			transaction = mSession.beginTransaction();
			user = (UserTbl) mSession.get(UserTbl.class, userId);
			transaction.commit();
		} catch (HibernateException he) {
			logger.error(he.getMessage());
		} finally {
			if (mSession != null) {
				mSession.close();
			}
		}
		logger.info("findById end"+Calendar.getInstance().getTime());
		return user;
	}

	public Set<RoutesTbl> findUserRoutes(long userId){
		logger.info("findUserRoutes start"+Calendar.getInstance().getTime());
		 UserTbl user = null;
		 Set<RoutesTbl> routesSet =null;
		try {
			mSession = HibernateUtil.getSession();
			transaction = mSession.beginTransaction();
			user = (UserTbl) mSession.get(UserTbl.class,userId);
			routesSet = user.getRoutesTbls();
			transaction.commit();
		} catch (HibernateException he) {
			logger.error(he.getMessage());
		} finally {
			if (mSession != null) {
				mSession.close();
			}
		}
		logger.info("findUserRoutes end"+Calendar.getInstance().getTime());
		return routesSet;
	}
	
	public int deleteUserPreviousSearchRoutes(UserTbl user){
		logger.info("deleteUserPreviousSearchRoutes start"+Calendar.getInstance().getTime());
		String deleteRoutes = "delete from RoutesTbl as routesTbl where routesTbl.userTbl.userId = :userId";
		try{
			mSession = HibernateUtil.getSession();
			transaction = mSession.beginTransaction();
		    Query deleteQuery = mSession.createQuery(deleteRoutes);
		    deleteQuery.setParameter("userId", user.getUserId());
			int totalCount = deleteQuery.executeUpdate();
			transaction.commit();
			System.out.println("totalCount "+totalCount);
			return totalCount;
		}catch(HibernateException he){
			he.printStackTrace();
			logger.error(he.getMessage());
			transaction.rollback();
		}finally{
			if(mSession != null){
				mSession.close();
			}
		}
		logger.info("deleteUserPreviousSearchRoutes start"+Calendar.getInstance().getTime());
		return 0;
	}
	
	
	
	public UserTbl updateUser(UserTbl user){
		logger.info("updateUser start"+Calendar.getInstance().getTime());
		try {
			mSession = HibernateUtil.getSession();
			transaction = mSession.beginTransaction();
			mSession.update(user);
			transaction.commit();
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (mSession != null) {
				mSession.flush();
				mSession.close();
			}
		}
		logger.info("updateUser end"+Calendar.getInstance().getTime());
		return user;
	
		
	}
}
