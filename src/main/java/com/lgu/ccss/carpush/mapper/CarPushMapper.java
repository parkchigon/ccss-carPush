package com.lgu.ccss.carpush.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgu.ccss.carpush.model.CarPushVO;


@Component
public class CarPushMapper {
	
	@Autowired
	CarPushMapperOracle carPsuhMapperOracle;
	
	@Autowired
	CarPushMapperAltibase carPushMapperAltibase;

	public List<CarPushVO> selectCarPushTargetList(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPushMapperAltibase.selectCarPushTargetList(carPushVo);
	}

	public int updateCarPushInitStatus(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPushMapperAltibase.updateCarPushInitStatus(carPushVo);
	}

	public int updateCarPushTargetStatus(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPushMapperAltibase.updateCarPushTargetStatus(carPushVo);
	}
	public int carPushUpdateReport(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPsuhMapperOracle.carPushUpdateReport(carPushVo);
	}
	
	public int updateCarPushTargetList(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPushMapperAltibase.updateCarPushTargetList(carPushVo);
	}

	public int insertCarPushSendHistory(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPsuhMapperOracle.insertCarPushSendHistory(carPushVo);
	}

	public int deleteCarPushTarget(CarPushVO carPushVo) {
		// TODO Auto-generated method stub
		return carPushMapperAltibase.deleteCarPushTarget(carPushVo);
	}
	
}
