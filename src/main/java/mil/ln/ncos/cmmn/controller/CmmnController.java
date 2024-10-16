package mil.ln.ncos.cmmn.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.cmmn.util.StringUtil;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CmmnController {

	private final CmmnService cmmnService;
	private final MessageSourceAccessor messageSource;
	/*
	 * private final CacheService cacheService;
	 * 
	 * @Value("${code.codeKey}")
	 * private String codeKey;
	 */

	@GetMapping("/codes")
	public @ResponseBody Map<String, Object> getList(HttpServletRequest req) throws Exception {
		Map<String, Object> codeMap = new HashMap<>();
		// codeMap.put("codes", cacheService.getCodeCacheData(codeKey));
		codeMap.put("codes", cmmnService.getSelectCodeList());
		return codeMap;
	}

	@GetMapping("/zoneList")
	public @ResponseBody Map<String, Object> getZoneList(HttpServletRequest req) throws Exception {
		Map<String, Object> zoneInfo = new HashMap<>();
		zoneInfo.put("zoneInfo", cmmnService.getZoneList(null));
		return zoneInfo;
	}

	@GetMapping("/managerList")
	public @ResponseBody Map<String, Object> getManagerList(HttpServletRequest req) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("info", cmmnService.getManagerList(null));
		return map;
	}

	@GetMapping("/korMap")
	public String gerKorMap(Model model) throws Exception {
		return "popup/korMap";
	}

	@GetMapping("/sessionChk")
	public ResponseEntity<Map<String, Object>> sessionChk(HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = req.getSession();
		if (session == null || session.getAttribute("user") == null) {
			result.put("session", "off");
			return ResponseEntity.ok().body(result);
		}
		result.put("session", "on");
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/whiteList")
	public ResponseEntity<Map<String, Object>> getWhiteList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", cmmnService.getWhiteList(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/watchList")
	public ResponseEntity<Map<String, Object>> getWatchList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		param.put("accountId", SessionData.getUserVo().getAccountId());
		return ResponseEntity.ok().body(cmmnService.getWatchList(param));
	}

	@GetMapping("/watchEquipStatus")
	public ResponseEntity<Map<String, Object>> getEquipStatus(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", cmmnService.getNetworkEquipementStatus(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveWhiteList")
	public ResponseEntity<Map<String, Object>> saveWhiteList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int rtn = cmmnService.saveWhiteList(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveLinkInfo")
	public ResponseEntity<Map<String, Object>> saveLinkInfo(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int rtn = cmmnService.saveLinkInfo(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/getSatelliteTrans")
	public ResponseEntity<Map<String, Object>> getSatelliteTrans(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("info", cmmnService.getSatelliteTrans(param));
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/getUnitInfos")
	public ResponseEntity<Map<String, Object>> getUnitInfos(@RequestParam Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", cmmnService.getUnitInfos(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/getShipInfos")
	public ResponseEntity<Map<String, Object>> getShipInfos(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("list", cmmnService.getShipInfos(param));
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deleteWhiteList")
	public ResponseEntity<Map<String, Object>> deleteWhiteList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int rtn = cmmnService.deleteWhiteList(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/updateWhitePolicyList")
	public ResponseEntity<Map<String, Object>> updateWhitePolicyList(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int rtn = cmmnService.updateWhitePolicyList(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveSatelliteTrans")
	public ResponseEntity<Map<String, Object>> saveSatelliteTrans(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		param.put("setter", SessionData.getUserVo().getAccountId());
		int rtn = cmmnService.saveSatelliteTrans(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/saveAlarmCheck")
	public ResponseEntity<Map<String, Object>> saveAlarmCheck(@RequestBody Map<String, Object> param,
			HttpServletRequest req) throws Exception {
		Map<String, Object> result = new HashMap<>();
		param.put("accountId", SessionData.getUserVo().getAccountId());
		int rtn = cmmnService.saveAlarmCheck(param);
		if (rtn > 0) {
			result.put("success_msg", messageSource.getMessage("msg.success"));
		} else {
			result.put("fail_msg", messageSource.getMessage("msg.fail"));
		}
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/makeJsonStr")
	@ResponseBody
	public Map<String, Object> makeJsonStr(HttpServletRequest req) throws Exception {
		Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
		log.debug("makeJsonStr.............");
		String sql = req.getParameter("sql");
		sql = sql.replaceAll("&lt;", "<");
		sql = sql.replaceAll("&gt;", ">");
		String[] sqls = sql.split(";");

		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://localhost:3306/ncos";
		String user = "root";
		String pwd = "ncos!1245";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pwd);
			stmt = con.createStatement();
			for (int i = 0; i < sqls.length; i++) {
				// rs = stmt.executeQuery("select * from dual");
				if (sqls[i] == null || sqls[i].equals("")) {
					break;
				}
				String paramSql = sqls[i];
				log.debug("paramSql:{}", paramSql);
				Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
				rs = stmt.executeQuery(paramSql);
				rsmd = rs.getMetaData();
				int cols = rsmd.getColumnCount();
				// log.debug("column count:{}",cols);
				for (int j = 1; j <= cols; j++) {
					log.debug("column index:{}", j);
					log.debug("columnName:{}", rsmd.getColumnName(j));
					String column = StringUtil.convertCamelCase(rsmd.getColumnName(j));
					log.debug("column:{}", column);
					jsonMap.put(column, "column" + j);
				}
				log.debug("jsonMap:{}", jsonMap);
				rtnMap.put("json" + (i + 1), jsonMap);
			}

			if (rs != null)
				rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return rtnMap;
	}

}