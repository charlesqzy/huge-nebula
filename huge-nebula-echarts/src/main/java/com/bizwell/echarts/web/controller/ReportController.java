package com.bizwell.echarts.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.ChartConfigVo;
import com.bizwell.echarts.bean.vo.FormHeader;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.JsonView;
import com.bizwell.echarts.common.ReportManager;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.ChartConfigService;
import com.bizwell.echarts.service.FormService;
import com.bizwell.echarts.service.ReportService;
import com.bizwell.echarts.web.BaseController;
import com.github.pagehelper.PageHelper;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Controller
@RequestMapping(value="/echarts/report")
public class ReportController extends BaseController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private ChartConfigService chartConfigService;
	
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getReport(String param, Integer userId) {
		
		JsonView jsonView = new JsonView();
		try {
			String code = JsonUtils.getString(param, "moduleType");
			if (!ReportManager.isSupport(code)) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL06.getCode(), ResponseCode.ECHARTS_FAIL06.getMessage());
			}
			
			ReportService reportService = ReportManager.getService(code);
			ResultData resultData = reportService.selectEcharts(param, userId);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), resultData);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return jsonView;
	}
	
	@RequestMapping(value = "/page/getData", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getPage(@RequestParam(value = "param", required = true) String param,
			@RequestParam(value = "id", required = true) Integer id, 
			@RequestParam(value = "userId", required = true) Integer userId, 
			@RequestParam(value = "curPage" , required = true) Integer curPage,
			@RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
		
		JsonView jsonView = new JsonView();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Integer cnt = 0;
		try {
			if (StringUtils.isEmpty(param) && id != null) {
				ChartConfigVo chartConfigVo = chartConfigService.getOne(id);
				param = chartConfigVo.getSqlConfig();				
			}
			
			PageHelper.startPage(curPage, pageSize);
			String code = JsonUtils.getString(param, "moduleType");
			if (!ReportManager.isSupport(code)) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL06.getCode(), ResponseCode.ECHARTS_FAIL06.getMessage());
			}
			
			List<FormHeader> headerList = new ArrayList<FormHeader>();
			List<SheetMetaData> list2 = getMetaData(param, userId);
			if (list2.size() >= 1) {
				getHeader(headerList, list2);
			}
			
			list = formService.selectList(param, userId);
			cnt = formService.selectCnt(param, userId);
			
			Map<String, Object> map = new HashMap<>();
			map.put("header", headerList);
			map.put("list", list);
			map.put("total", cnt);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), map);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return jsonView;
	}
	
	private List<SheetMetaData> getMetaData(String data, Integer userId) {
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		// 获取维度字段名称
		List<SheetMetaData> dimensions = JsonUtils.getFields(data, "dimension", "metadataId", userId);
		for (SheetMetaData sheetMetaData : dimensions) {
			list.add(sheetMetaData);
		}
		
		// 获取数值字段
		List<SheetMetaData> measures = JsonUtils.getFields(data, "measure1", "metadataId", userId);
		for (SheetMetaData sheetMetaData : measures) {
			list.add(sheetMetaData);
		}
		return list;
	}
	
	private void getHeader(List<FormHeader> headerList, List<SheetMetaData> list) {
		
		for (SheetMetaData sheetMetaData : list) {
			FormHeader formHeader = new FormHeader();
			formHeader.setProp(sheetMetaData.getFieldColumn());
			formHeader.setLabel(sheetMetaData.getFieldNameNew());
			headerList.add(formHeader);
		}
	}
	
}
