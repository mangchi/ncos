package mil.ln.ncos.train.service;


import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.dao.DAO;


@Slf4j
@RequiredArgsConstructor
@Service
public class TrainServiceImpl implements TrainService {
    
	@Value("${spring.profiles.active}")
	private String activeProfile;
	private final DAO dao;
	
	@Value("${cmd}")
    private String cmd;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTrainList(Map<String, Object> map) throws Exception {
		return (List<Map<String,Object>>)dao.selectPage("Train.selectTrainCount","Train.selectTrainList",map);
	}


	@Override
	public int execTrain(Map<String, Object> map) throws Exception {
		//String cmd = "/home/user/ncos/train_run.sh";
		log.debug("train cmd:{}",cmd);
		File cmdFile = new File(cmd);
		if(!cmdFile.exists()) {
			log.error("train_run.sh is not found.....................");
			return 0;
		}
		CommandLine cmdLine = CommandLine.parse("sh "+cmd);
		try {
    		log.debug("shell cmd:{}","sh "+cmd);
    		DefaultExecutor executor = new DefaultExecutor();
    		executor.execute(cmdLine);
    	}catch (Exception e){
    		e.printStackTrace();
    		return 2;
   	    }
		return 1;
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getTrainInfo() throws Exception {
		return (Map<String, Object>)dao.selectOptionalObject("Train.selectTrainInfo",null).orElseGet(() -> null);
	}

	


}
