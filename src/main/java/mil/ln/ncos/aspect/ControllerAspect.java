package mil.ln.ncos.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.annotation.NoAuth;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.annotation.ReportScan;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.util.StringUtil;
import mil.ln.ncos.cmmn.vo.LogVo;
import mil.ln.ncos.cmmn.vo.PageInfo;
import mil.ln.ncos.exception.BizException;
import mil.ln.ncos.func.service.FuncService;
import mil.ln.ncos.log.service.LogService;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ControllerAspect {

	@Pointcut("execution(* mil.ln.ncos..*.*Controller.*(..))")
	public void commonPointcut() {
	}

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${darkMode}")
	private String darkMode;
	@Value("${whiteMode}")
	private String whiteMode;

	@Value("${cryptoMode}")
	private String cryptoMode;

	@Value("${crypto.key}")
	private String cryptoModeKey;

	private final LogService logService;

	private final FuncService funcService;

	private final CmmnService cmmnService;

	@SuppressWarnings("unchecked")
	@Before("commonPointcut()")
	public void callBeforeController(JoinPoint joinPoint) throws Throwable {
		Map<String, Object> param = null;

		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Page page = methodSignature.getMethod().getAnnotation(Page.class);
			for (Object obj : joinPoint.getArgs()) {
				if (obj instanceof HttpServletRequest) {
					/*
					 * HttpServletRequest request = (HttpServletRequest) obj;
					 * Enumeration<String> enu = request.getParameterNames();
					 * while(enu.hasMoreElements()) {
					 * String name = enu.nextElement();
					 * log.debug("name:{}",name,",value:{}",request.getParameter(name));
					 * }
					 */
					if (!methodSignature.getName().equals("logout")) {
						/*
						 * HttpServletRequest request = (HttpServletRequest) obj; String refer =
						 * request.getHeader("Referer"); HttpSession session = request.getSession();
						 * log.debug("session:{}",session);
						 */
					}
				}
				if (obj instanceof Map) {
					param = (Map<String, Object>) obj;
					if (page != null) {
						if (param.containsKey("pageNo") && param.containsKey("rowPerPage")) {
							int startRow = Integer.parseInt(String.valueOf(param.get("rowPerPage")))
									* (Integer.parseInt(String.valueOf(param.get("pageNo"))) - 1);
							param.put("startRow", startRow);
							PageInfo pageInfo = PageInfo.builder()
									.pageNo(Integer.parseInt(String.valueOf(param.get("pageNo"))))
									.rowPerPage(Integer.parseInt(String.valueOf(param.get("rowPerPage"))))
									.orderBy(page.sort().equals("ASC") ? "DESC" : "ASC").build();
							pageInfo.setStartRow(startRow);
							param.put("pageInfo", pageInfo);
						}
						if (param.containsKey("schSrcIp")) {
							param.put("schSrcIp", StringUtil.ipToLong(String.valueOf(param.get("schSrcIp"))));
						}

						if (param.containsKey("schDstIp")) {
							param.put("schDstIp", StringUtil.ipToLong(String.valueOf(param.get("schDstIp"))));
						}

						if (param.containsKey("schIpAddress")) {
							param.put("schIpAddress", StringUtil.ipToLong(String.valueOf(param.get("schIpAddress"))));
						}
						if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
							if (cryptoMode.equals("Y") && param.containsKey("userId")) {

								param.put("userId",
										ScpDbUtil.scpEnc(String.valueOf(param.get("userId")), cryptoModeKey));

							}
						}
					} else {
						if (param.containsKey("srcIp")) {
							param.put("srcIp", StringUtil.ipToLong(String.valueOf(param.get("srcIp"))));
						}
						if (param.containsKey("srcMask")) {
							param.put("srcMask", StringUtil.ipToLong(String.valueOf(param.get("srcMask"))));
						}
						if (param.containsKey("dstIp")) {
							param.put("dstIp", StringUtil.ipToLong(String.valueOf(param.get("dstIp"))));
						}
						if (param.containsKey("dstMask")) {
							param.put("dstMask", StringUtil.ipToLong(String.valueOf(param.get("dstMask"))));
						}
						if (param.containsKey("ipAddress")) {
							param.put("ipAddress", StringUtil.ipToLong(String.valueOf(param.get("ipAddress"))));
						}
						if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
							if (cryptoMode.equals("Y") && param.containsKey("userId")) {

								param.put("userId",
										ScpDbUtil.scpEnc(String.valueOf(param.get("userId")), cryptoModeKey));

							}
						}
					}

				}
			}

			log.debug(methodSignature.getName() + " param:{}", joinPoint.getArgs());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ControllerAspect error:", e);
		}

	}
	/*
	 * private Object getValueFromField(Field field, Class<?> clazz, Object obj) {
	 * for (Method method : clazz.getMethods()) {
	 * String methodName = method.getName();
	 * if ((methodName.startsWith("get") && methodName.length() ==
	 * field.getName().length() + 3)
	 * || (methodName.startsWith("is") && methodName.length() ==
	 * field.getName().length() + 2)) {
	 * if (methodName.toLowerCase().endsWith(field.getName().toLowerCase())) {
	 * try {
	 * return method.invoke(obj);
	 * } catch (Exception e) {
	 * //e.printStackTrace();
	 * }
	 * }
	 * }
	 * }
	 * return null;
	 * }
	 */

	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut = "commonPointcut()", returning = "returnValue")
	public void callAfterController(JoinPoint joinPoint, Object returnValue) throws BizException, Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		ReportScan reportScan = methodSignature.getMethod().getAnnotation(ReportScan.class);
		LogVo logVo = new LogVo();
		HttpServletRequest req = null;
		boolean isLog = false;
		try {
			for (Object obj : joinPoint.getArgs()) {
				if (obj instanceof HttpServletRequest) {
					req = (HttpServletRequest) obj;
				}

			}
			if (returnValue instanceof ResponseEntity) {
				Page page = methodSignature.getMethod().getAnnotation(Page.class);
				ResponseEntity<Map<String, Object>> res = (ResponseEntity<Map<String, Object>>) returnValue;
				Map<String, Object> map = res.getBody();
				if (page != null) {
					for (Object obj : joinPoint.getArgs()) {
						if (obj instanceof Map) {
							Map<String, Object> param = (Map<String, Object>) obj;
							if (param.containsKey("pageInfo")) {
								PageInfo pageInfo = (PageInfo) param.get("pageInfo");
								if (pageInfo.getTotCount() % pageInfo.getRowPerPage() == 0) {
									pageInfo.setTotPage(Integer.parseInt(
											String.valueOf(pageInfo.getTotCount() / pageInfo.getRowPerPage())));
								} else {
									pageInfo.setTotPage(Integer.parseInt(
											String.valueOf(pageInfo.getTotCount() / pageInfo.getRowPerPage())) + 1);
								}
								// pageInfo.setTotPage(Integer.parseInt(String.valueOf(map.get("totCount")))/Integer.parseInt(String.valueOf(param.get("rowPerPage")))+1);
								pageInfo.setIsFirstPage(pageInfo.getPageNo() == 1 ? true : false);
								pageInfo.setIsLastPage(pageInfo.getPageNo() == pageInfo.getTotPage() ? true : false);
								pageInfo.setIsFirstPerPage(pageInfo.getPageNo() > 10 ? true : false);
								// pageInfo.setIsLastPerPage((pageInfo.getTotPage()-pageInfo.getPageNo())/10 >
								// 1?false:true);
								map.put("pageInfo", pageInfo);
								// map.put("totPage",
								// Integer.parseInt(String.valueOf(map.get("totCount")))/Integer.parseInt(String.valueOf(param.get("rowPerPage")))+1);
							}
						}

					}
				}
				if (map.containsKey("list")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
					list.forEach(m -> {
						toDecList(m);
					});
				}
				if (map.containsKey("linkInfoList")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("linkInfoList");
					list.forEach(m -> {
						toDecList(m);
					});
				}
				if (map.containsKey("netEquipList")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("netEquipList");
					list.forEach(m -> {
						toDecList(m);
					});
				}
				if (map.containsKey("appendData")) {
					Map<String, Object> appendMap = (Map<String, Object>) map.get("appendData");
					if (appendMap.containsKey("linkInfoList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("linkInfoList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (appendMap.containsKey("shipList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("shipList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					// shipList
					if (appendMap.containsKey("topoList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("topoList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (appendMap.containsKey("errorList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("errorList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (appendMap.containsKey("netEquipList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("errorList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (appendMap.containsKey("assetZoneList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("assetZoneList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
				}

				if (map.containsKey("info")) {
					Map<String, Object> info = (Map<String, Object>) map.get("info");
					toDecInfo(info);
				}

				if (map.containsKey("info")) {
					Map<String, Object> info = (Map<String, Object>) map.get("info");
					toDecInfo(info);
				}
			}

			if (returnValue instanceof ModelAndView) {
				isLog = true;
				if (methodSignature.getName().equals("login") || methodSignature.getName().equals("sysInfo")
						|| methodSignature.getName().equals("ssoBusiness")
						|| methodSignature.getName().equals("checkAuth")
						|| methodSignature.getName().equals("agentProc")) {
					isLog = false;
					return;
				}
				logVo.setWorkCodeId("4");
				ModelAndView mv = (ModelAndView) returnValue;
				mv.addObject("uiMode", whiteMode);
				if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
					mv.addObject("uiMode", darkMode);
				}
				mv.addObject("activeProfile", activeProfile);
				if (mv.getViewName().indexOf("threatDisp") > -1) {
					logVo.setWorkUiId("1");
				} else if (mv.getViewName().indexOf("assetDisp") > -1) {
					logVo.setWorkUiId("2");
				} else if (mv.getViewName().indexOf("funcDisp") > -1 || mv.getViewName().indexOf("FuncDisp") > -1) {
					logVo.setWorkUiId("3");
				} else if (mv.getViewName().indexOf("userAudit") > -1) {
					logVo.setWorkUiId("4");
				} else if (mv.getViewName().indexOf("netTopo") > -1) {
					logVo.setWorkUiId("5");
				} else if (mv.getViewName().indexOf("Main") > -1) {

					logVo.setWorkUiId("6");
				}

				else if (mv.getViewName().indexOf("assetMng") > -1) {
					logVo.setWorkUiId("10");
				} else if (mv.getViewName().indexOf("userList") > -1) {

					logVo.setWorkUiId("11");
				} else if (mv.getViewName().indexOf("envConf") > -1) {

					logVo.setWorkUiId("12");
				} else if (mv.getViewName().indexOf("rptFrmMng") > -1) {

					logVo.setWorkUiId("13");
				} else if (mv.getViewName().indexOf("rptSchlMng") > -1) {

					logVo.setWorkUiId("14");
				} else if (mv.getViewName().indexOf("rptMng") > -1 || mv.getViewName().indexOf("clipreport") > -1) {

					logVo.setWorkUiId("15");
				} else {
					log.debug("########################################");
					log.debug("mv.getViewName():{}", mv.getViewName());
					log.debug("########################################");
					logVo.setWorkUiId("16");
				}

				if (null == SessionData.getUserVo()) {
					logVo.setWorkUiId("16");
					logVo.setResult(0);
					logVo.setWorkContent("서버 재시작으로 로그아웃");
				} else {
					logVo.setWorkUiId("16");
					logVo.setAccountId(SessionData.getUserVo().getAccountId());
					logVo.setResult(1);
					logVo.setWorkContent("화면 조회");
				}
			} else if (returnValue instanceof ResponseEntity) {
				if (null == SessionData.getUserVo()) {
					logVo.setResult(0);
					logVo.setWorkCodeId("1");
					logVo.setWorkUiId("7");
					logVo.setWorkContent("로그아웃");
				} else {
					logVo.setAccountId(SessionData.getUserVo().getAccountId());
					logVo.setResult(1);
					log.debug("methodSignature:{}", methodSignature.getName());
					if (methodSignature.getName().equals("getWhiteList")) {
						isLog = true;
						logVo.setWorkCodeId("4");
						logVo.setWorkUiId("8");
						logVo.setWorkContent("화면 조회");
					} else if (methodSignature.getName().equals("getSatelliteTrans")) {
						isLog = true;
						logVo.setWorkCodeId("4");
						logVo.setWorkUiId("9");
						logVo.setWorkContent("화면 조회");
					} else if (methodSignature.getName().indexOf("save") > -1
							|| methodSignature.getName().indexOf("delete") > -1)
						if (req != null) {
							isLog = true;
							logVo.setAccountId(SessionData.getUserVo().getAccountId());
							if (methodSignature.getName().indexOf("Satellite") > -1) {
								logVo.setWorkUiId("9");
							} else if (methodSignature.getName().indexOf("White") > -1) {
								logVo.setWorkUiId("8");

							} else if (methodSignature.getName().indexOf("Link") > -1) {
								logVo.setWorkUiId("5");
							} else if (methodSignature.getName().indexOf("Alarm") > -1) {
								logVo.setWorkUiId("8");
							} else if (methodSignature.getName().indexOf("Asset") > -1) {
								logVo.setWorkUiId("10");
							} else if (methodSignature.getName().indexOf("Env") > -1) {
								logVo.setWorkUiId("8");
							} else if (methodSignature.getName().indexOf("RptFrm") > -1) {
								logVo.setWorkUiId("13");
							} else if (methodSignature.getName().indexOf("RptSch") > -1) {
								logVo.setWorkUiId("14");
							} else if (methodSignature.getName().indexOf("ThreatAnalysis") > -1) {
								String refer = req.getHeader("Referer");
								if (refer.endsWith("threatDisp")) {
									logVo.setWorkUiId("1");
								} else {
									logVo.setWorkUiId("6");
								}

							} else if (methodSignature.getName().indexOf("UserAccount") > -1) {
								logVo.setWorkUiId("11");
							} else {
								logVo.setWorkUiId("16");
							}

							if (null != SessionData.getUserVo()) {
								log.debug("sqlId:{}", SessionData.getUserVo().getSqlId());
								if (SessionData.getUserVo().getSqlId().indexOf("update") > -1) {
									logVo.setWorkCodeId("6");
									logVo.setWorkContent("데이터 수정");
								} else if (SessionData.getUserVo().getSqlId().indexOf("insert") > -1) {
									logVo.setWorkCodeId("5");
									logVo.setWorkContent("데이터 추가");
								} else if (SessionData.getUserVo().getSqlId().indexOf("delete") > -1) {
									logVo.setWorkCodeId("7");
									logVo.setWorkContent("데이터 삭제");
								}
							}
						}
				}

			}
			if (logVo.getWorkUiId() == null || logVo.getWorkUiId().equals("")) {
				logVo.setWorkUiId("16");// 미지정
			}
			if (reportScan != null) {
				ResponseEntity<Map<String, Object>> res = (ResponseEntity<Map<String, Object>>) returnValue;
				Map<String, Object> map = res.getBody();
				log.debug("map:{}", map);
				if (res.getStatusCodeValue() == 200) {
					cmmnService.setReportKey(map, req);
				}

			}

		} catch (Exception e) {
			logVo.setResult(0);
			logVo.setWorkContent(e.getMessage());
			e.printStackTrace();
		} finally {
			if (isLog) {
				logService.saveUserAction(logVo);
			}
			log.debug("return:{}", returnValue);

		}

	}

	private void toDecInfo(Map<String, Object> info) {
		if (info.containsKey("srcIp") && info.get("srcIp") instanceof Boolean == false) {
			if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화

				info.put("srcIp", StringUtil
						.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(info.get("srcIp")), cryptoModeKey))));
				info.put("dstIp", StringUtil
						.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(info.get("dstIp")), cryptoModeKey))));

			} else {
				info.put("srcIp", StringUtil.longToIp(Long.parseLong(String.valueOf(info.get("srcIp")))));
				info.put("dstIp", StringUtil.longToIp(Long.parseLong(String.valueOf(info.get("dstIp")))));
			}
		}
		if (info.containsKey("ipAddress")) {
			if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화

				info.put("ipAddress", StringUtil.longToIp(
						Long.parseLong(ScpDbUtil.scpDec(String.valueOf(info.get("ipAddress")), cryptoModeKey))));

			} else {

				info.put("ipAddress",
						StringUtil.longToIp(Long.parseLong(String.valueOf(info.get("ipAddress")))));
			}
		}

		if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화

			if (info.containsKey("assetName")) {
				info.put("assetName", ScpDbUtil.scpDec(String.valueOf(info.get("assetName")), cryptoModeKey));
			}
			if (info.containsKey("affiliation")) {
				info.put("affiliation", ScpDbUtil.scpDec(String.valueOf(info.get("affiliation")), cryptoModeKey));
			}
			if (info.containsKey("userId")) {
				info.put("userId", ScpDbUtil.scpDec(String.valueOf(info.get("userId")), cryptoModeKey));
			}
			if (info.containsKey("userName")) {
				info.put("userName", ScpDbUtil.scpDec(String.valueOf(info.get("userName")), cryptoModeKey));
			}
			if (info.containsKey("username")) {
				info.put("username", ScpDbUtil.scpDec(String.valueOf(info.get("username")), cryptoModeKey));
			}

		}

		if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1 && cryptoMode.equals("Y")) {

			if (info.containsKey("userId")) {
				info.put("userId",
						ScpDbUtil.scpDec(String.valueOf(info.get("userId")), cryptoModeKey));
			}
			if (info.containsKey("username")) {
				info.put("username",
						ScpDbUtil.scpDec(String.valueOf(info.get("username")), cryptoModeKey));
			}

		}
	}

	private void toDecList(Map<String, Object> m) {
		Map<String, Object> item = (Map<String, Object>) m;

		if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화
			if (item.containsKey("assetName")) {

				item.put("assetName", ScpDbUtil.scpDec(String.valueOf(item.get("assetName")), cryptoModeKey));

			}
			if (item.containsKey("affiliation")) {

				item.put("affiliation", ScpDbUtil.scpDec(String.valueOf(item.get("affiliation")), cryptoModeKey));

			}
			if (item.containsKey("userId")) {

				item.put("userId", ScpDbUtil.scpDec(String.valueOf(item.get("userId")), cryptoModeKey));

			}
			if (item.containsKey("userName")) {

				item.put("userName", ScpDbUtil.scpDec(String.valueOf(item.get("userName")), cryptoModeKey));

			}

			if (item.containsKey("username")) {

				item.put("username", ScpDbUtil.scpDec(String.valueOf(item.get("username")), cryptoModeKey));

			}
		}

		if (item.containsKey("srcIp") && item.get("srcIp") instanceof Boolean == false) {
			if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화
				// if (methodSignature.getName().equals("getWhiteList") &&
				// cryptoMode.equals("Y")) {//암호화된 필드 복호화

				item.put("srcIp", StringUtil.longToIp(
						Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("srcIp")), cryptoModeKey))));
				item.put("dstIp", StringUtil.longToIp(
						Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("dstIp")), cryptoModeKey))));

			} else {
				item.put("srcIp", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("srcIp")))));
				item.put("dstIp", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("dstIp")))));
			}
		}
		if (item.containsKey("srcMask")) {
			item.put("srcMask",
					StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("srcMask")))));
			item.put("dstMask",
					StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("dstMask")))));
		}
		if (item.containsKey("ipAddress")) {
			if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화

				item.put("ipAddress", StringUtil.longToIp(
						Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("ipAddress")), cryptoModeKey))));

			} else {
				item.put("ipAddress",
						StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("ipAddress")))));
			}

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AfterThrowing(pointcut = "commonPointcut()", throwing = "ex")
	public void errorInterceptor(JoinPoint joinPoint, Throwable ex) throws Exception {
		ex.printStackTrace();
		Map<String, Object> param = new HashMap();
		param.put("status", "1");
		param.put("cscName", "3");
		param.put("workType", "9");
		param.put("reason", "화면 조회 실패");
		funcService.saveFuncOperation(param);
		// log.error(ex.getMessage());
	}

	@Around("@annotation(mil.ln.ncos.annotation.NoAuth) && @ annotation(target)")
	public Object calllNoAuth(ProceedingJoinPoint joinPoint, NoAuth target) throws Throwable {
		log.debug("ControllerAspect calllNoAuth:" + joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName());
		return joinPoint.proceed();

	}

}
