package com.lgu.ccss.carpush.service.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgu.ccss.carpush.constant.CarPushStatusConst;
import com.lgu.ccss.carpush.mapper.CarPushDao;
import com.lgu.ccss.carpush.model.CarPushVO;

@Service
public class StatusInitServiceImpl implements StatusInitService{
	private final Logger logger = LoggerFactory.getLogger(StatusInitServiceImpl.class);
	
	@Autowired
	CarPushDao carPushDao;
	
	@Override
	public boolean doTask() {
		// TODO Auto-generated method stub
		CarPushVO carPushVo = new CarPushVO();
		logger.debug("########################################################");
		logger.debug("#                  UPDATE INIT STATUS                  #");
		logger.debug("########################################################");
		return carPushDao.updateCarPushInitStatus(carPushVo, CarPushStatusConst.PUSH_STATUS_SEND_MSG_READY);
	}
}
