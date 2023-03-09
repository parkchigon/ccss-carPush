package com.lgu.ccss.carpush.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgu.ccss.carpush.config.CarPushConf;
import com.lgu.ccss.carpush.constant.CarPushConst;
import com.lgu.ccss.carpush.constant.CarPushStatusConst;
import com.lgu.ccss.carpush.model.CarPushVO;

@Component
public class CarPushDao {
	private final Logger logger = LoggerFactory.getLogger(CarPushDao.class);
	
	@Autowired
	private CarPushConf carPushConf;
	@Autowired
	private CarPushMapper carPushMapper;
	
	public List<CarPushVO> getTargetList(int selectSize) {
		// 예약전송시간 이전 메시지 + 대기상태  메시지를 조회
		
		CarPushVO carPushVo = new CarPushVO();
		Date nowDt = new Date();
		carPushVo.setSendDt(new SimpleDateFormat(CarPushConst.DATE_FORMAT_DATETIME).format(nowDt));
		carPushVo.setMsgStatus(CarPushStatusConst.PUSH_STATUS_SEND_MSG_READY);
		carPushVo.setSvrId(carPushConf.getSvrId());
		carPushVo.setRowCount(selectSize);
		List<CarPushVO> targetList = carPushMapper.selectCarPushTargetList(carPushVo);

		return targetList;
	}
	public boolean updateCarPushInitStatus(CarPushVO carPushVo,String status) {
		carPushVo.setMsgStatus(status);
		carPushVo.setUpdId(carPushConf.getSystemId());
		int resultStatus = carPushMapper.updateCarPushInitStatus(carPushVo);
		logger.debug("updateCarPushInitStatus Result : "+resultStatus);
		
		
		return true;
	}
	public boolean carPushUpdateStatus(CarPushVO carPushVo,String status) {
		carPushVo.setMsgStatus(status);
		carPushVo.setUpdId(carPushConf.getSystemId());
		int resultStatus = carPushMapper.updateCarPushTargetStatus(carPushVo);
		if(resultStatus<1) {
			// 실패시 처리
			logger.debug("Status update Fail");
			return false;
		}
		return true;
	}
	public void carPushProcessFinish(CarPushVO carPushVo,String code) {
		carPushVo.setMsgStatus(code);
		carPushVo.setSendTryCnt(carPushVo.getSendTryCnt());
		carPushVo.setUpdId(carPushConf.getSystemId());
		carPushVo.setSendDt(new SimpleDateFormat(CarPushConst.DATE_FORMAT_DATETIME).format(new Date()));
		carPushMapper.insertCarPushSendHistory(carPushVo); // 성공 이력 저장
		logger.info("insertCarPushSendHistory CTN ["+carPushVo.getRcvReport()+"] MSGID ["+carPushVo.getMsgId()+"] STATUS ["+code+"]");
		carPushMapper.deleteCarPushTarget(carPushVo);
		logger.info("deleteCarPushTarget MSGID ["+carPushVo.getMsgId()+"]");
	}
	public void carPushUpdateCount(CarPushVO carPushVo) {
		carPushVo.setSendTryCnt(carPushVo.getSendTryCnt()+1);
		carPushMapper.updateCarPushTargetList(carPushVo); // 시도 횟수 업데이트
	}
	public boolean carPushUpdateReport(String msgId,String status) {
		CarPushVO carPushVo = new CarPushVO();
		carPushVo.setMsgId(msgId);
		carPushVo.setMsgStatus(status);
		carPushVo.setUpdId(carPushConf.getSystemId());
		int resultStatus = carPushMapper.carPushUpdateReport(carPushVo);
		if(resultStatus<1) {
			// 실패시 처리
			logger.debug("Status update Fail");
			return false;
		}
		return true;
	}
}
