package com.bizwell.echarts.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.JsonView;
import com.bizwell.echarts.common.ReportManager;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.ReportService;
import com.bizwell.echarts.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Controller
@RequestMapping(value="/echarts/report")
public class ReportController extends BaseController {
	
	
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getReport(String param) {
		
		JsonView jsonView = new JsonView();
		try {
			String code = JsonUtils.getString(param, "moduleType");
			if (!ReportManager.isSupport(code)) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL06.getCode(), ResponseCode.ECHARTS_FAIL06.getMessage());
			}
			
			ReportService reportService = ReportManager.getService(code);
			ResultData resultData = reportService.selectEcharts(param);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), resultData);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return jsonView;
	}
	
}
