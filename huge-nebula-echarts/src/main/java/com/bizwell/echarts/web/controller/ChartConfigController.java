package com.bizwell.echarts.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.bean.domain.ChartLog;
import com.bizwell.echarts.bean.dto.ChartConfigParam;
import com.bizwell.echarts.bean.vo.ChartConfigVo;
import com.bizwell.echarts.bean.vo.ResultLocation;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.JsonView;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.mapper.ChartLogMapper;
import com.bizwell.echarts.service.ChartConfigService;
import com.bizwell.echarts.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
// 操作配置信息所走controller
@Controller
@RequestMapping(value="/echarts/chart")
public class ChartConfigController extends BaseController {
	
	@Autowired
	private ChartConfigService chartConfigService;
	
	@Autowired
	private ChartLogMapper chartLogMapper;
	
	// 编辑或者保存图表时,保存其各类配置信息
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonView save(ChartConfigParam param) {
		
		JsonView jsonView = new JsonView();
		try {
			String echartType = JsonUtils.getString(param.getSqlConfig(), "echartType");
			// "01"表示用户没有添加维度或者数值,不需要保存
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
	
	// 点击仪表盘下文件,获取所有图表的位置信息以及相关数据
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public JsonView get(
			@RequestParam(value = "panelId",required = false) Integer panelId,
			@RequestParam(value = "panelUuid",required = false) String panelUuid,
			@RequestParam(value = "userId", defaultValue = "0",required = false) Integer userId) {
		
		
		//记录查询日志
		ChartLog record = new ChartLog();
		record.setPanelId(panelId);
		record.setPanelUuid(panelUuid);
		record.setUserId(userId);
		chartLogMapper.insert(record);
		
		
		JsonView jsonView = new JsonView();
		try {
			ResultLocation resultLocation = new ResultLocation();
			// 仪表盘id为空时,不查询
			//if (null != panelId) {
				resultLocation = chartConfigService.selectLocation(panelId,panelUuid);				
			//}
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), resultLocation);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	// 通过id,获取单条配置信息
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
	
	// 更新配置信息
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
	
	// 更新位置信息
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
	
	// 删除图表
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
	
}
