package com.lgu.ccss.carpush.mapper;

import java.util.List;

import com.lgu.ccss.carpush.model.CarPushVO;
import com.lgu.ccss.config.annontation.Slave;

@Slave
public interface CarPushMapperAltibase {
	List<CarPushVO> selectCarPushTargetList(CarPushVO carPushVO);
	int updateCarPushInitStatus(CarPushVO carPushVO);
	int updateCarPushTargetStatus(CarPushVO carPushVO);
	int updateCarPushTargetList(CarPushVO carPushVO);
	int deleteCarPushTarget(CarPushVO carPushVO);
}
