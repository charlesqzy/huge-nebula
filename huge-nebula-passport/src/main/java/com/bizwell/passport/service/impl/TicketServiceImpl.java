package com.bizwell.passport.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.passport.bean.domain.Ticket;
import com.bizwell.passport.common.AESCodec;
import com.bizwell.passport.common.Constants;
import com.bizwell.passport.exception.PassportException;
import com.bizwell.passport.exception.ResponseCode;
import com.bizwell.passport.service.RedisService;
import com.bizwell.passport.service.TicketService;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Service
public class TicketServiceImpl implements TicketService {
	
	private static final String SEP = "-";
	
	@Autowired
	private RedisService redisService;
	
	private String buildStoreKey(String ticketCode) {
		
		return Constants.STORED_PREFIX + ticketCode;
	}
	
	// 存储ticket
	@Override
	public String storeTicke(Ticket ticket) {
		
		String ticketCode = generate(ticket);
		String key = buildStoreKey(ticketCode);
		redisService.setValue(key, ticket, Constants.TICKET_EXPIRED_TIME);
		return ticketCode;
	}
	
	// 校验ticket
	@Override
	public Ticket validateTicket(String ticketCode) {

		try {
			String key = buildStoreKey(ticketCode);
			Object obj = redisService.getValue(key);
			if (obj != null && obj instanceof Ticket) {
				return (Ticket) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new PassportException(ResponseCode.PASSPORT_FAIL06.getCode(), ResponseCode.PASSPORT_FAIL06.getMessage());
		}
	}
	
	// 删除ticket
	@Override
	public void removeTicket(String ticketCode) {

		try {
			String key = buildStoreKey(ticketCode);
			redisService.removeKey(key);
		} catch (Exception e) {
			throw new PassportException(ResponseCode.PASSPORT_FAIL07.getCode(), ResponseCode.PASSPORT_FAIL07.getMessage());
		}
	}
	
	// 生成ticket
	private String generate(Ticket ticket) {
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(ticket.getUserId());
			sb.append(SEP);
			sb.append(ticket.getUserName());
			sb.append(SEP);
			sb.append(ticket.getGenteateTime());
			String orgingTicket = sb.toString();
			
			String encryptTicket = AESCodec.encrypt(orgingTicket, Constants.AES_KEY);
			if (StringUtils.isEmpty(encryptTicket)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL05.getCode(), ResponseCode.PASSPORT_FAIL05.getMessage());
			} else {
				return encryptTicket;
			}
		} catch (Exception e) {
			throw new PassportException(ResponseCode.PASSPORT_FAIL05.getCode(), ResponseCode.PASSPORT_FAIL05.getMessage());
		}
	}

}
