package com.bizwell.echarts.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.bean.domain.FolderInfo;
import com.bizwell.echarts.bean.dto.FolderParam;
import com.bizwell.echarts.bean.vo.FolderVo;
import com.bizwell.echarts.common.JsonView;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.FolderService;
import com.bizwell.echarts.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
 *
 */
@Controller
@RequestMapping(value="/echarts/folder")
public class FolderController extends BaseController {
	
	@Autowired
	private FolderService folderService;
	
	@RequestMapping(value = "/getFolder", method = RequestMethod.POST)
	@ResponseBody
	public JsonView getFolder(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "userId", required = true) Integer userId) {
		
		JsonView jsonView = new JsonView();
		try {
			if (null == userId) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL01.getCode(), ResponseCode.ECHARTS_FAIL01.getMessage());
			}
			List<FolderVo> list = folderService.selectFolder(userId);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return jsonView;
	}
	
	@RequestMapping(value = "/deleteFloder", method = RequestMethod.POST)
	@ResponseBody
	public JsonView deleteFloder(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id", required = true) Integer id) {
		
		JsonView jsonView = new JsonView();
		try {
			if (null == id) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL02.getCode(), ResponseCode.ECHARTS_FAIL02.getMessage());
			}

			folderService.deleteFolder(id);
			FolderParam param = new FolderParam();
			param.setParentId(id);
			List<FolderInfo> list = folderService.selectByParam(param);
			for (FolderInfo folderInfo : list) {
				folderService.deleteFolder(folderInfo.getId());
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
	
	@RequestMapping(value = "/updateFloder", method = RequestMethod.POST)
	@ResponseBody
	public JsonView updateFloder(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "childId", required = true) Integer childId) {
		
		JsonView jsonView = new JsonView();
		try {
			if (null == id) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL03.getCode(), ResponseCode.ECHARTS_FAIL03.getMessage());
			}
			
			if (null == childId) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL02.getCode(), ResponseCode.ECHARTS_FAIL02.getMessage());
			}
			
			folderService.updateFolder(id, childId);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/saveFloder", method = RequestMethod.POST)
	@ResponseBody
	public JsonView saveFloder(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "userId", required = true) Integer userId,
			@RequestParam(value = "folderName", required = true) String folderName,
			@RequestParam(value = "parentId", required = true) Integer parentId,
			@RequestParam(value = "level", required = true) Integer level) {
		
		JsonView jsonView = new JsonView();
		try {
			if (null == userId) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL01.getCode(), ResponseCode.ECHARTS_FAIL01.getMessage());
			}
			if (StringUtils.isEmpty(folderName)) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL04.getCode(), ResponseCode.ECHARTS_FAIL04.getMessage());
			}
			if (null == level) {
				throw new EchartsException(ResponseCode.ECHARTS_FAIL05.getCode(), ResponseCode.ECHARTS_FAIL05.getMessage());
			}
			
			FolderVo folderVo = folderService.saveFolder(userId, folderName, parentId, level);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), folderVo);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	@ResponseBody
	public JsonView updateStatus(@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "status", required = true) String status,
			@RequestParam(value = "shareRemarks", required = true) String shareRemarks) {
		
		JsonView jsonView = new JsonView();
		try {
			folderService.updateStatus(id, status, shareRemarks);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (EchartsException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return jsonView;
	}
	
	@RequestMapping(value = "/updateShowMore", method = RequestMethod.POST)
	@ResponseBody
	public JsonView updateShowMore(@RequestParam(value = "id", required = true) Integer id,
			@RequestParam(value = "showMore", required = true) Boolean showMore) {
		
		JsonView jsonView = new JsonView();
		try {
			folderService.updateShowMore(id, showMore);
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
