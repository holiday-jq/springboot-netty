package com.holiday.matcloud.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.holiday.matcloud.dto.ResultDto;

@RestController
@RequestMapping("/wechat")
public class FileController {
	
	private final String PATH = "D://data/";

	@RequestMapping(value = "/upload", method = RequestMethod.POST)	
	public ResultDto fileUpload(@RequestParam("file") MultipartFile file){
		long fileSize = file.getSize();
		String fileName = file.getOriginalFilename(); // 文件名字.	
		String FileContentType = file.getContentType(); // 文件类型
		try {
			File TargetFile = new File(PATH);
			if (!TargetFile.exists()) {
				TargetFile.mkdirs();
			}			
			if (!FileContentType.equals("")) {
				// 输出文件到目录
				FileOutputStream out = new FileOutputStream(PATH + fileName);
				out.write(file.getBytes());
				out.flush();
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResultDto.ok("", "上传失败");
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("fileName", fileName);
		resultMap.put("fileType", FileContentType);
		resultMap.put("fileSize", fileSize);
		resultMap.put("filePath", PATH);
		return ResultDto.ok(resultMap, "上传成功");
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void download(@RequestParam(value = "fileName", required = true) String fileName, 
			HttpServletResponse response) throws IOException {
		InputStream inStream = null;
		final int size = 1024;
		final byte[] b = new byte[size];
		int len = 0;
		try {
			inStream = new FileInputStream(PATH + URLEncoder.encode(fileName, "iso8859-1"));
			// 设置输出的格式
            response.reset();
			response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            // 写出
            while ((len = inStream.read(b)) > 0) {
            	response.getOutputStream().write(b, 0, len);
            }            
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭资源
				inStream.close();
				response.getOutputStream().flush();
	            response.getOutputStream().close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
