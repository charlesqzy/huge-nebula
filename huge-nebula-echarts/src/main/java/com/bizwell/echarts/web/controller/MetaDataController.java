package com.bizwell.echarts.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.common.JsonView;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.SheetMetaDataService;
import com.bizwell.echarts.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年5月31日
 *
 */
// 刷新缓存再map中的数据,用户添加表格或者追加数据时调用
@Controller
@RequestMapping(value="/echarts/metadata")
public class MetaDataController extends BaseController {
	
	@Autowired
	private SheetMetaDataService sheetMetaDataService;
	
	// 刷新数据
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	@ResponseBody
	public JsonView refresh(Integer userId) {
		
		JsonView jsonView = new JsonView();
		try {
			sheetMetaDataService.refresh(userId);
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
