package mil.ln.ncos.cmmn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil{
	
	

	public static Map<String, Object> upload(List<MultipartFile> files, HttpServletRequest req, String uploadTarget) {
		log.debug("## Entrance File Upload Utility ");
		log.debug("┌──────────── Fild Upload ─────────────────────────────────────────────────────────────────────────────────────────────");
		
		if(files.size() < 1) {
			log.debug("│ [ Error ] Upload File Count : 0  ");
			log.debug("└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			return null;
		}
		// Result Map
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		// Loop List
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<String, Object>();

		// 저장 경로 설정
		//String root = req.getSession().getServletContext().getRealPath("");
		String path = "";
		log.debug("│ Upload Path : "+path);
		
		String ofn = ""; // 오리지널 파일명
		String nfn = ""; // 업로드 되는 파일명
		String uid = ""; // 업로드 되는 ID
		String size = ""; // 업로드 되는 size

		int successCnt = 0;
		int failCnt = 0;
		int totalCnt = 0;

		File dir = new File(path);
		if (!dir.isDirectory()) {
			dir.setExecutable(false, true);
			dir.setReadable(true);
			dir.setWritable(false, true);
			dir.mkdirs();
		}

		for (MultipartFile mpf : files) {
			uid = UUID.randomUUID().toString();
			ofn = mpf.getOriginalFilename();
			nfn = uid + "." + ofn.substring(ofn.lastIndexOf(".") + 1);
			size = mpf.getSize() + "";
			
			m = new HashMap<String, Object>();

			m.put("id", uid);
			m.put("name", ofn);
			m.put("size", size);
			m.put("newName", nfn);

			try {
				File uf = new File(path + nfn);
				mpf.transferTo(uf);
				m.put("message", "success");
				successCnt++;
			} catch ( IOException  e) {
				log.error("error:", e );
				m.put("message", "fails");
				failCnt++;
			} finally {
				rtnList.add(m);
			}

			totalCnt++;
		}

		rtnMap.put("filelist", rtnList);
		rtnMap.put("success", successCnt);
		rtnMap.put("failed", failCnt);
		rtnMap.put("count", totalCnt);
		return rtnMap;
	}

	public static void down(HttpServletRequest req, HttpServletResponse res, String downloadTarget, String fileName) throws FileNotFoundException {
		log.debug("## Entrance File down Utility ");
		log.debug("┌──────────── Fild Download ───────────────────────────────────────────────────────────────────────────────────────────");
		
		//String root = req.getSession().getServletContext().getRealPath("");
		String path = "";
		String fullPath = path+fileName;
		
		
		File downloadFile = new File(fullPath);

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		String mimeType = req.getSession().getServletContext().getMimeType(fullPath);
		
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		

		if(!downloadFile.exists())
		{
			log.debug("│ [ Error ] File Not Found Real Path => "+fullPath);
			log.debug("└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
			throw new FileNotFoundException("file with path: " + fullPath + " was not found.");
		}                                                                                                  
		
		log.debug("│ [ Success ] File Download Real Path => "+fullPath);
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			res.setContentType(mimeType);
			res.setContentLength((int) downloadFile.length());
			res.setHeader(headerKey, headerValue);
			outStream = res.getOutputStream();
			inputStream = new FileInputStream(downloadFile);
			
			byte[] buffer = new byte[100];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

		} catch (IOException e) {
			log.error("error:", e );
		}
		finally {
			try{if(null != inputStream) {inputStream.close();}}catch(IOException e) {log.error(e.getMessage());}
			try{if(null != outStream) {outStream.close();}}catch(IOException e) {log.error(e.getMessage());}
		}
		log.debug("└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
	}
	
	public static void down(HttpServletRequest req, HttpServletResponse res, File downloadFile) throws FileNotFoundException {
		log.debug("## Entrance File down Utility ");
		log.debug("┌──────────── Excel Fild Download ─────────────────────────────────────────────────────────────────────────────────────");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		String mimeType = "application/octet-stream";
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			res.setContentType(mimeType);
			res.setContentLength((int) downloadFile.length());
			res.setHeader(headerKey, headerValue);
			outStream = res.getOutputStream();
			inputStream = new FileInputStream(downloadFile);
			
			byte[] buffer = new byte[100];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

		} catch (IOException e) {
			log.error("error:", e );
		}
		finally {
			try{if(null != inputStream) {inputStream.close();}}catch(IOException e) {log.error(e.getMessage());}
			try{if(null != outStream) {outStream.close();}}catch(IOException e) {log.error(e.getMessage());}
		}
		log.debug("└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
	}
	/**
	  * @Method Name	: copyFile
	  * @author			: choi
	  * @Date			: 2019. 11. 21.
	  * @Description	: srcPath의 파일을 toPath 파일로 복사함
	  * @History		: 
	  * @param srcPath
	  * @param toPath
	  * @return
	  * @throws IOException
	  */
	public boolean copyFile(String srcPath, String toPath) throws IOException{
		return copyStream(new FileInputStream(srcPath), toPath);
	}
	
	/**
	  * @Method Name	: copyStream
	  * @author			: choi
	  * @Date			: 2019. 11. 21.
	  * @Description	: InputStream 을 읽어서 toPath 경로의 파일을 생성함
	  * @History		: 
	  * @param in
	  * @param toPath
	  * @return
	  * @throws IOException
	  */
	public boolean copyStream(InputStream in, String toPath) throws IOException{
		
		// 디렉토리 생성
		File dir = new File(toPath.substring(0, toPath.lastIndexOf('/')));
		if(!dir.isDirectory()) {
			dir.setExecutable(false, true);
			dir.setReadable(true);
			dir.setWritable(false, true);
			dir.mkdirs();
		}
		
		FileOutputStream out = null;
		try{
			int len;
			byte[] bytes = new byte[2048];
			out = new FileOutputStream(toPath);
			while((len = in.read(bytes, 0, 2048))>0){
				out.write(bytes, 0, len);
			}
			return true;
		} finally {
			if(in!=null){
				try{ in.close(); } catch(IOException ignore){log.error("copyStream in IOException..");}
			}
			if(out!=null){
				try{ out.close(); } catch(IOException ignore){log.error("copyStream out IOException..");}
			}
		}
	}
}