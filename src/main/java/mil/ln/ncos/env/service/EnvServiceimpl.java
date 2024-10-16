package mil.ln.ncos.env.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.dao.DAO;

@RequiredArgsConstructor
@Service
public class EnvServiceimpl implements EnvService {

	private final DAO dao;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${cryptoMode}")
	private String cryptoMode;

	@Value("${crypto.key}")
	private String cryptoModeKey;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getConfList(Map<String, Object> map) throws Exception {
		return (List<Map<String, Object>>) dao.selectPage("Env.selectConfCount", "Env.selectConfList", map);
	}

	@Override
	public int saveEnvConf(Map<String, Object> map) throws Exception {
		if (cryptoMode.equals("N")) {
			if (map.containsKey("settingId") && map.get("settingId") != null
					&& !map.get("settingId").toString().trim().equals("")) {
				return dao.update("Env.updateAccountSetting", map);
			} else {
				return dao.update("Env.insertAccountSetting", map);
			}
		} else {// ncos 무결성검증.
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Date date = new Date(ts.getTime());
			String accountSettingTime = DateUtil.formatDate(date, "yyyy-MM-dd HH:mm.ss");
			Map<String, Object> salt = dao.selectMap("Cmmn.selectIntegritySalt", null)
					.orElseGet(() -> new HashMap<String, Object>());
			final String saltKey = "accountSettings";
			String integrity = (String) map.get("accountId")
					+ (String) map.get("alarmLevel")
					+ (String) map.get("warningLevel")
					+ (map.get("alarmStatus") == null ? "false"
							: (((String) map.get("alarmStatus")).equals("1") ? "true" : "false"))
					+ (map.get("warningStatus") == null ? "false"
							: (((String) map.get("warningStatus")).equals("1") ? "true" : "false"))
					+ (map.get("sessionControlStatus") == null ? "false"
							: (((String) map.get("sessionControlStatus")).equals("1") ? "true" : "false"))
					+ accountSettingTime.substring(0, 10);// yyyy-mm-dd

			integrity = integrity + ScpDbUtil.scpDec((String) salt.get(saltKey), cryptoModeKey);

			map.put("accountSettingTime", accountSettingTime);
			map.put("integrity", integrity);
			if (map.containsKey("settingId") && map.get("settingId") != null
					&& !map.get("settingId").toString().trim().equals("")) {
				return dao.update("Env.updateAccountSettingNcos", map);
			} else {
				return dao.update("Env.insertAccountSettingNcos", map);
			}
		}
	}
}
