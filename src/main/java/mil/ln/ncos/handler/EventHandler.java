package mil.ln.ncos.handler;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.event.AppEvent;
import mil.ln.ncos.exception.BizException;

@RequiredArgsConstructor
@Component
public class EventHandler {

	@SuppressWarnings("unchecked")
	@Async
	@TransactionalEventListener
	public void handle(AppEvent event) {

		Map<String, Object> map = (Map<String, Object>) event.getObj();
		// try {
		if (null == map.get("type")) {
			throw new BizException(ErrorCode.EVENT_HANDLE_TYPE_IS_NULL);// "EventHandler type is null"
		} else {
			if (map.get("type").equals("WORK")) {

			} else {
				throw new BizException(ErrorCode.EVENT_HANDLE_TYPE_IS_NOT_VALID);
			}
		}

		// }catch(Exception e) {
		// log.error("error:", e );
		// }
	}

}
