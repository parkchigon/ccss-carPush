package com.lgu.ccss.carpush.mapper;


import com.lgu.ccss.carpush.model.CarPushVO;
import com.lgu.ccss.config.annontation.Master;

@Master
public interface CarPushMapperOracle {
	int insertCarPushSendHistory(CarPushVO carPushVO);
	int carPushUpdateReport(CarPushVO carPushVO);
}
