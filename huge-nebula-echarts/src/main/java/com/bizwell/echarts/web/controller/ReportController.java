package com.bizwell.echarts.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
// 编辑图表或者创建图表所走controller
@Controller
@RequestMapping(value="/echarts/report")
public class ReportController extends BaseController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private ChartConfigService chartConfigService;
	
	// 获取图表需要数据
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getReport(String param, Integer userId) {
		
		JsonView jsonView = new JsonView();
		try {
			// 通过moduleType获取出code,判断是否是系统支持的图表
			String code = JsonUtils.getString(param, "moduleType");
			if (!ReportManager.isSupport(code)) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL06.getCode(), ResponseCode.ECHARTS_FAIL06.getMessage());
			}
			
			//通过code获取对应的service
			ReportService reportService = ReportManager.getService(code);
			// 查询出图表数据
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
	
	// 分页查询出表格的数据
	@RequestMapping(value = "/page/getData", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getPage(@RequestParam(value = "param", required = true) String param,
			@RequestParam(value = "id", required = true) Integer id, 
			@RequestParam(value = "userId", required = true) Integer userId, 
			@RequestParam(value = "curPage" , required = true) Integer curPage,
			@RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
		
		
		System.out.println("param= " +param);
		
		String tableName = "xls_16d506e966a257c240adaed164fdbdcc_u16_s01" ;
		
		JsonView jsonView = new JsonView();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Integer cnt = 0;
		try {
			// 判断有没有param,假如没有的话通过id在表中查询出param
			if (StringUtils.isEmpty(param) && id != null) {
				ChartConfigVo chartConfigVo = chartConfigService.getOne(id);
				param = chartConfigVo.getSqlConfig();				
			}
			
			// 开启分页查询
			PageHelper.startPage(curPage, pageSize);
			// 通过moduleType获取出code,判断是否是系统支持的图表
			String code = JsonUtils.getString(param, "moduleType");
			if (!ReportManager.isSupport(code)) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL06.getCode(), ResponseCode.ECHARTS_FAIL06.getMessage());
			}
			
			List<FormHeader> headerList = new ArrayList<FormHeader>();
//			// 获取用户选中的所有维度与数值,用于组装成表头
			List<SheetMetaData> list2 = getMetaData(param, userId);
			if (list2.size() >= 1) {
				// 将数值组装进表头
				getHeader(headerList, list2);
			}
			
			
			// 分页查询出表格数据
			list = formService.selectList(param, userId);
			// 查询出数据的总条数
			cnt = formService.selectCnt(param, userId);
			// 将表头,数据,总条数封装进map中
			Map<String, Object> map = new HashMap<>();
			
			List<FormHeader> newHeaderList = getNewHaderList(list, headerList);
			
			map.put("header", newHeaderList);
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

	private List<FormHeader> getNewHaderList(List<Map<String, Object>> list, List<FormHeader> headerList) {
		List<FormHeader> newHeaderList = new ArrayList<FormHeader>();
		
		if(list.size()>0){
			 for(String key :list.get(0).keySet()){
				 String column = key.split("_")[0];
				 
				 String aggregate = ReportManager.getAggregate(key);
				 
				 String label="";
				 for(FormHeader header : headerList){
					 if(column.equals(header.getProp())){
						 label= header.getLabel();break;
					 }
				 }
				 
				 FormHeader header = new FormHeader();
				 header.setLabel(label+aggregate);
				 header.setProp(key);
				 newHeaderList.add(header);
			 }
		}
		return newHeaderList;
	}


	
	// 获取用户选中的所有维度与数值,用于组装成表头
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
	
	// 将数值组装进表头
	private void getHeader(List<FormHeader> headerList, List<SheetMetaData> list) {
		
		for (SheetMetaData sheetMetaData : list) {
			FormHeader formHeader = new FormHeader();
			formHeader.setProp(sheetMetaData.getFieldColumn());
			formHeader.setLabel(sheetMetaData.getFieldNameNew());
			headerList.add(formHeader);
		}
	}
	
}
