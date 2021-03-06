package com.gafis.xj.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.gafis.xj.model.ReportInfo;
import com.gafis.xj.service.IReportInfoService;

import net.sf.json.JSONObject;

@Controller
public class ReportController {
	
	@Autowired
	private IReportInfoService reportInfoService;
	
	/**
	 * 接收上报数据
	 * @param info
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/collector/report")
	public String report(ReportInfo info){
		int result=0;
		JSONObject jsonObject=new JSONObject();
		if(info!=null){
			info.setCreateDate(new Date());
			try{
				result=reportInfoService.saveReportInfo(info);
				if(result==1){
					jsonObject.put("code", "200");
					return jsonObject.toString();
				}else{
					jsonObject.put("code", "202");
					return jsonObject.toString();
				}
			}catch(Exception e){
				jsonObject.put("code", "201");
				return jsonObject.toString();
			}
		}else{
			jsonObject.put("code", "201");
			return jsonObject.toString();
		}
	}
	
	/**
	 * 跳转离线上报数据导入页面
	 * @return
	 */
	@RequestMapping("/reportEdit")
	public String toReportEdit(){
		return "report/reportEdit";
	}
	
	
 
	
	
	/**
	 * 导入离线上报数据
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/importReport")
	public String importReportFile(@RequestParam("fileToUpload") MultipartFile file){
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = file.getInputStream();
	            InputStreamReader is = new InputStreamReader(inputStream, "UTF-8");
	            BufferedReader br = new BufferedReader(is);
				String s = null;
				while((s = br.readLine())!=null){
					reportInfoService.saveImportReport(s);
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		return "导入完成";
	}
	
	 
}
