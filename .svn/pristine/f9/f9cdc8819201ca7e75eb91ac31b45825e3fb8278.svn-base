package mil.ln.ncos.rpt.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.dao.DAO;



@Slf4j
@RequiredArgsConstructor
@Component
public class RptGenerator {
	
	private final DAO dao;
	
	@Value("${prodMode}") 
	private String prodMode;
	
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void genReport() throws Exception {
		if(prodMode.equals("Y")) {
			log.debug("genReport...............");
			List<Map<String, Object>> rptJobList = (List<Map<String, Object>>) dao.selectList("Rpt.selectRptJob", new HashMap<>());
			for (Map<String, Object> rptJob : rptJobList) {
				log.debug("rptJob:{}", rptJob);
				if(rptJob.get("makeRpt").equals("Y") || rptJob.get("afterMakeRpt").equals("Y")) {
					dao.updateByJob("Rpt.updateRptSchNextTime",rptJob);
					dao.updateByJob("Rpt.insertRptMng",rptJob);
				}
			}
		}
		
		
	}

}
