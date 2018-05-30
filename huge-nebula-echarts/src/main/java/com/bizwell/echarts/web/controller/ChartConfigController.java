package com.bizwell.echarts.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.bean.dto.ChartConfigParam;
import com.bizwell.echarts.bean.vo.ChartConfigVo;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.JsonView;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.ChartConfigService;
import com.bizwell.echarts.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
@Controller
@RequestMapping(value="/echarts/chart")
public class ChartConfigController extends BaseController {
	
	@Autowired
	private ChartConfigService chartConfigService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonView save(ChartConfigParam param) {
		
		JsonView jsonView = new JsonView();
		try {
			String echartType = JsonUtils.getString(param.getSqlConfig(), "echartType");
			if (!"01".equals(echartType)) {
				chartConfigService.save(param);
			}
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public JsonView get(@RequestParam(value = "userId", required = true) Integer userId,
			@RequestParam(value = "panelId", required = true) Integer panelId) {
		
		JsonView jsonView = new JsonView();
		try {
			List<Object> list = chartConfigService.selectLocation(userId, panelId);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/getOne", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getOne(@RequestParam(value = "id", required = true) Integer id) {
		
		JsonView jsonView = new JsonView();
		try {
			if (null == id) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL07.getCode(), ResponseCode.ECHARTS_FAIL07.getMessage());
			}
			
			ChartConfigVo chartConfigVo = chartConfigService.getOne(id);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), chartConfigVo);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public JsonView update(ChartConfigParam param) {
		
		JsonView jsonView = new JsonView();
		try {
			
			chartConfigService.update(param);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/updateLocation", method = RequestMethod.POST)
	@ResponseBody
	public JsonView updateLocation(ChartConfigParam param) {
		
		JsonView jsonView = new JsonView();
		try {
			chartConfigService.updateLocation(param.getLocations());
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public JsonView delete(@RequestParam(value = "id", required = true) Integer id) {
		
		JsonView jsonView = new JsonView();
		try {
			
			chartConfigService.delete(id);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/getShare", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getShare(@RequestParam(value = "userId", required = true) Integer userId,
			@RequestParam(value = "panelId", required = true) Integer panelId) {
		
		JsonView jsonView = new JsonView();
		try {
			List<Object> list = chartConfigService.selectLocation(userId, panelId);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
}
