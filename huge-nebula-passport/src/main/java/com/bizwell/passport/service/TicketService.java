package com.bizwell.passport.service;

import com.bizwell.passport.bean.domain.Ticket;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public interface TicketService {

	public String storeTicke(Ticket ticket);
	
	public Ticket validateTicket(String ticketCode);
	
	public void removeTicket(String ticketCode);
}
