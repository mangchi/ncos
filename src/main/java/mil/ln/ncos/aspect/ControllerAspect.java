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

import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.annotation.NoAuth;
import mil.ln.ncos.annotation.Page;
import mil.ln.ncos.annotation.ReportScan;
import mil.ln.ncos.auth.service.AuthService;
import mil.ln.ncos.cmmn.SessionData;
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.cmmn.util.StringUtil;
import mil.ln.ncos.cmmn.vo.LogVo;
import mil.ln.ncos.cmmn.vo.PageInfo;
import mil.ln.ncos.exception.BizException;
import mil.ln.ncos.func.service.FuncService;
import mil.ln.ncos.log.service.LogService;
import mil.ln.ncos.user.vo.UserVo;

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

	@Value("${crypto.key1}")
	private String cryptoModeKey1;

	private final LogService logService;

	private final FuncService funcService;

	private final CmmnService cmmnService;
	
	private final AuthService authService;

	@SuppressWarnings("unchecked")
	@Before("commonPointcut()")
	public void callBeforeController(JoinPoint joinPoint) throws Throwable {
		Map<String, Object> param = null;

		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Page page = methodSignature.getMethod().getAnnotation(Page.class);
			for (Object obj : joinPoint.getArgs()) {

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
						if (cryptoMode.equals("Y")) {
							if (param.containsKey("schSrcIp")) {
								param.put("schSrcIp",
										ScpDbUtil.scpEnc(
												String.valueOf(
														StringUtil.ipToLong(String.valueOf(param.get("schSrcIp")))),
												cryptoModeKey1));
							}
							if (param.containsKey("schDstIp")) {
								param.put("schDstIp",
										ScpDbUtil.scpEnc(
												String.valueOf(
														StringUtil.ipToLong(String.valueOf(param.get("schDstIp")))),
												cryptoModeKey1));
							}
							if (param.containsKey("schIpAddress")) {
								param.put("schIpAddress",
										ScpDbUtil.scpEnc(
												String.valueOf(
														StringUtil.ipToLong(String.valueOf(param.get("schIpAddress")))),
												cryptoModeKey1));
							}
							if (param.containsKey("userId")) {
								param.put("userId",
										ScpDbUtil.scpEnc(String.valueOf(param.get("userId")), cryptoModeKey1));
							}

						} else {
							if (param.containsKey("schSrcIp")) {
								param.put("schSrcIp", StringUtil.ipToLong(String.valueOf(param.get("schSrcIp"))));
							}
							if (param.containsKey("schDstIp")) {
								param.put("schDstIp", StringUtil.ipToLong(String.valueOf(param.get("schDstIp"))));
							}
							if (param.containsKey("schIpAddress")) {
								param.put("schIpAddress",
										StringUtil.ipToLong(String.valueOf(param.get("schIpAddress"))));
							}
						}

					} else {
						if (cryptoMode.equals("Y")) {
							if (param.containsKey("srcIp") && param.get("srcIp").toString().length() > 1) {
								param.put("srcIp",
										ScpDbUtil.scpEnc(
												String.valueOf(StringUtil.ipToLong(String.valueOf(param.get("srcIp")))),
												cryptoModeKey1));
							}

							if (param.containsKey("dstIp") && param.get("dstIp").toString().length() > 1) {
								param.put("dstIp",
										ScpDbUtil.scpEnc(
												String.valueOf(StringUtil.ipToLong(String.valueOf(param.get("dstIp")))),
												cryptoModeKey1));
							}

							if (param.containsKey("ipAddress")) {
								param.put("ipAddress",
										ScpDbUtil.scpEnc(
												String.valueOf(
														StringUtil.ipToLong(String.valueOf(param.get("ipAddress")))),
												cryptoModeKey1));
							}
							if (param.containsKey("userId")) {
								param.put("userId",
										ScpDbUtil.scpEnc(String.valueOf(param.get("userId")), cryptoModeKey1));
							}
							if (param.containsKey("schIpAddress")) {
								param.put("schIpAddress",
										ScpDbUtil.scpEnc(
												String.valueOf(
														StringUtil.ipToLong(String.valueOf(param.get("schIpAddress")))),
												cryptoModeKey1));
							}
						} else {
							if (param.containsKey("srcIp") && param.get("srcIp").toString().length() > 1) {
								param.put("srcIp", StringUtil.ipToLong(String.valueOf(param.get("srcIp"))));
							}
							if (param.containsKey("dstIp") && param.get("dstIp").toString().length() > 1) {
								param.put("dstIp", StringUtil.ipToLong(String.valueOf(param.get("dstIp"))));
							}
							if (param.containsKey("ipAddress")) {
								param.put("ipAddress", StringUtil.ipToLong(String.valueOf(param.get("ipAddress"))));
							}
							if (param.containsKey("schIpAddress")) {
								param.put("schIpAddress",
										StringUtil.ipToLong(String.valueOf(param.get("schIpAddress"))));
							}
						}
						/*
						if (param.containsKey("srcMask")) {
							param.put("srcMask", StringUtil.ipToLong(String.valueOf(param.get("srcMask"))));

						}
						if (param.containsKey("dstMask")) {
							param.put("dstMask", StringUtil.ipToLong(String.valueOf(param.get("dstMask"))));
						}
						*/
					}

				}
			}

			log.debug(methodSignature.getName() + " param:{}", joinPoint.getArgs());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ControllerAspect error:", e);
		}

	}

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
								pageInfo.setIsFirstPage(pageInfo.getPageNo() == 1 ? true : false);
								pageInfo.setIsLastPage(
										pageInfo.getPageNo().equals(pageInfo.getTotPage()) ? true : false);
								pageInfo.setIsFirstPerPage(pageInfo.getPageNo() > 10 ? true : false);
								map.put("pageInfo", pageInfo);
							}
						}

					}
				}
				if (null != map && map.containsKey("list")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
					list.forEach(m -> {
						toDecList(m);
					});
				}
				if (null != map && map.containsKey("linkInfoList")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("linkInfoList");
					list.forEach(m -> {
						toDecList(m);
					});
				}
				if (null != map && map.containsKey("netEquipList")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("netEquipList");
					list.forEach(m -> {
						toDecList(m);
					});
				}
				if (null != map && map.containsKey("appendData")) {
					Map<String, Object> appendMap = (Map<String, Object>) map.get("appendData");
					if (null != appendMap && appendMap.containsKey("linkInfoList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("linkInfoList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (null != appendMap && appendMap.containsKey("shipList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("shipList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (null != appendMap && appendMap.containsKey("topoList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("topoList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (null != appendMap && appendMap.containsKey("errorList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("errorList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (null != appendMap && appendMap.containsKey("netEquipList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("errorList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
					if (null != appendMap && appendMap.containsKey("assetZoneList")) {
						List<Map<String, Object>> list = (List<Map<String, Object>>) appendMap.get("assetZoneList");
						list.forEach(m -> {
							toDecList(m);
						});
					}
				}

				if (null != map && map.containsKey("info")) {
					Map<String, Object> info = (Map<String, Object>) map.get("info");
					if(info != null) {
						toDecInfo(info);
					}
					
				}

			}

			if (returnValue instanceof ModelAndView) {
				isLog = true;
				if (methodSignature.getName().equals("login") || methodSignature.getName().equals("logout") || methodSignature.getName().equals("sysInfo")
						|| methodSignature.getName().equals("ssoBusiness")
						|| methodSignature.getName().equals("checkAuth")
						|| methodSignature.getName().equals("agentProc")) {
					isLog = false;
					return;
				}
				if (null == SessionData.getUserVo()) {
					isLog = false;
					return;
				} else {
					logVo.setWorkCodeId("4");
					ModelAndView mv = (ModelAndView) returnValue;
					mv.addObject("uiMode", whiteMode);
					if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) {
						mv.addObject("uiMode", darkMode);
					}
					mv.addObject("activeProfile", activeProfile);
					mv.addObject("authorization", SessionData.getUserVo().getAuthorization());
					mv.addObject("auth", SessionData.getUserVo().getAuthorization());
					
					
					JsonObject json = new JsonObject();
					UserVo user = authService.getCurUserAccount(SessionData.getUserVo(), req);
					logVo.setTerminalIp(SessionData.getUserVo().getTerminalIp());
					if(user != null) {
						
						logVo.setAccountId(user.getAccountId());
						json.addProperty("accountId", user.getAccountId());
						json.addProperty("userId", user.getUserId());
						json.addProperty("username", user.getUsername());
						json.addProperty("auth", user.getAuthorization());
						json.addProperty("affiliationId", user.getAffiliationId());
						json.addProperty("phoneNo", user.getPhoneNo());
						json.addProperty("classId", user.getClassId());
						json.addProperty("alarmStatus", user.getAlarmStatus());
						json.addProperty("alarmLevel", user.getAlarmLevel());
						json.addProperty("warningStatus", user.getWarningStatus());
						json.addProperty("warningLevel", user.getWarningLevel());
						json.addProperty("isLogin", user.getIsLogin());
						log.debug("user.getSessionControlStatus:{}",user.getSessionControlStatus());
						if (user.getSessionControlStatus() == 0) {
							json.addProperty("sessionTm", -1);
						} else {
							if(activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1 ) {
								json.addProperty("sessionTm", 5 * 60);
							}
							else {
								json.addProperty("sessionTm", 60 * 60);
							}
							
						}
						if(null != user.getLastSuccessAccessDate()) {
							json.addProperty("connectTime", DateUtil.formatDate(user.getLastSuccessAccessDate(), "HH:mm:ss"));
						}
						else {
							json.addProperty("connectTime", DateUtil.getFrmtDate(null, "HH:mm:ss"));
						}
                        
						mv.addObject("sessionData", json.toString());
						mv.addObject("authorization",user.getAuthorization());
						mv.addObject("auth", user.getAuthorization());
					}
					
					String viewName = mv.getViewName();
					if (viewName != null) {
						if (viewName.indexOf("logout") > -1) {
							isLog = false;
							return;
						}
						if (viewName.indexOf("threatDisp") > -1) {
							logVo.setWorkUiId("1");
							logVo.setWorkContent("사이버 위협 전시 화면 조회");

						} else if (viewName.indexOf("assetDispo") > -1) {
							logVo.setWorkUiId("16");
							logVo.setWorkContent("함내 자산 위치 전시 화면 조회");
						} else if (viewName.indexOf("assetDisp") > -1) {
							logVo.setWorkUiId("2");
							logVo.setWorkContent("자산 상태 전시 화면 조회");
						} else if (viewName.indexOf("funcDisp") > -1 || viewName.indexOf("FuncDisp") > -1) {
							logVo.setWorkUiId("3");
							logVo.setWorkContent("기능 동작 전시 화면 조회");
						} else if (viewName.indexOf("userAudit") > -1) {
							logVo.setWorkUiId("4");
							logVo.setWorkContent("사용자 행위 정보 화면 조회");
						} else if (viewName.indexOf("netTopo") > -1) {
							logVo.setWorkUiId("5");
							logVo.setWorkContent("네트워크 토폴리지 전시 화면 조회");
						} else if (viewName.indexOf("Main") > -1) {
							logVo.setWorkUiId("6");
							if (activeProfile.equals("navy")) {
								logVo.setWorkContent("함정 메인 화면 화면 조회");
							} else {
								logVo.setWorkContent("육상 메인 화면 화면 조회");
							}
						} else if (viewName.indexOf("assetMng") > -1) {
							logVo.setWorkUiId("10");
							logVo.setWorkContent("자산 관리 화면 조회");
						} else if (viewName.indexOf("userList") > -1) {
							logVo.setWorkUiId("11");
							logVo.setWorkContent("사용자 관리 화면 조회");
						} else if (viewName.indexOf("envConf") > -1) {
							logVo.setWorkUiId("12");
							logVo.setWorkContent("시스템 설정 화면 조회");
						} else if (viewName.indexOf("rptFrmMng") > -1) {
							logVo.setWorkUiId("13");
							logVo.setWorkContent("보고서 서식 관리 화면 조회");
						} else if (viewName.indexOf("rptSchlMng") > -1) {
							logVo.setWorkUiId("14");
							logVo.setWorkContent("보고서 스케쥴 관리 화면 조회");
						} else if (viewName.indexOf("rptMng") > -1 || viewName.indexOf("clipreport") > -1) {
							logVo.setWorkUiId("15");
							logVo.setWorkContent("보고서 관리 화면 조회");
						} else {
							logVo.setWorkUiId("17");
							logVo.setWorkContent("미지정 화면 조회");
						}
					}
					else {
						isLog = false;
						return;
					}
					

					logVo.setResult(1);
					logVo.setAccountId(SessionData.getUserVo().getAccountId());

				}

			} else if (returnValue instanceof ResponseEntity) {
				if (null == SessionData.getUserVo()) {
					isLog = false;
					return;
				} else {
					logVo.setTerminalIp(SessionData.getUserVo().getTerminalIp());
					logVo.setAccountId(SessionData.getUserVo().getAccountId());
					logVo.setResult(1);
					if (methodSignature.getName().equals("getWhiteList")) {
						isLog = true;
						logVo.setWorkCodeId("4");
						logVo.setWorkUiId("8");
						logVo.setWorkContent("회이트 리스트 정책설정 화면 조회");
					} else if (methodSignature.getName().equals("getSatelliteTrans")) {
						isLog = true;
						logVo.setWorkCodeId("4");
						logVo.setWorkUiId("9");
						logVo.setWorkContent("위성 전송 환경 설정 화면 조회");
					} else if (methodSignature.getName().indexOf("save") > -1
							|| methodSignature.getName().indexOf("delete") > -1) {
						isLog = true;
						if (methodSignature.getName().indexOf("Satellite") > -1) {
							logVo.setWorkUiId("9");
						} else if (methodSignature.getName().indexOf("White") > -1) {
							logVo.setWorkUiId("8");

						} else if (methodSignature.getName().indexOf("Link") > -1) {
							logVo.setWorkUiId("5");
						} else if (methodSignature.getName().indexOf("Alarm") > -1) {
							isLog = false;
							return;
							// logVo.setWorkUiId("8");
						} else if (methodSignature.getName().indexOf("Asset") > -1) {
							logVo.setWorkUiId("10");
						} else if (methodSignature.getName().indexOf("Env") > -1) {
							isLog = false;
							return;
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
							isLog = false;
							return;
						}

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
			if (reportScan != null) {
				ResponseEntity<Map<String, Object>> res = (ResponseEntity<Map<String, Object>>) returnValue;
				Map<String, Object> map = res.getBody();
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
				info.put("srcIp", StringUtil.isEmpty(String.valueOf(info.get("srcIp"))) ? ""
						: StringUtil.longToIp(
								Long.parseLong(ScpDbUtil.scpDec(String.valueOf(info.get("srcIp")), cryptoModeKey1))));
				info.put("dstIp", StringUtil.isEmpty(String.valueOf(info.get("dstIp"))) ? ""
						: StringUtil.longToIp(
								Long.parseLong(ScpDbUtil.scpDec(String.valueOf(info.get("dstIp")), cryptoModeKey1))));

			} else {
				log.debug("" + info.get("srcIp"));
				info.put("srcIp", StringUtil.isEmpty(String.valueOf(info.get("srcIp"))) ? ""
						: StringUtil.longToIp(Long.parseLong(String.valueOf(info.get("srcIp")))));
				info.put("dstIp", StringUtil.isEmpty(String.valueOf(info.get("dstIp"))) ? ""
						: StringUtil.longToIp(Long.parseLong(String.valueOf(info.get("dstIp")))));
			}
		}
		if (info.containsKey("ipAddress")) {
			if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화
				info.put("ipAddress", StringUtil.longToIp(
						Long.parseLong(ScpDbUtil.scpDec(String.valueOf(info.get("ipAddress")), cryptoModeKey1))));
			} else {

				info.put("ipAddress", StringUtil.longToIp(Long.parseLong(String.valueOf(info.get("ipAddress")))));
			}
		}

		if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화
			if (info.containsKey("assetName")) {
				info.put("assetName", ScpDbUtil.scpDec(String.valueOf(info.get("assetName")), cryptoModeKey1));
			}
			if (info.containsKey("affiliation")) {
				info.put("affiliation", ScpDbUtil.scpDec(String.valueOf(info.get("affiliation")), cryptoModeKey1));
			}
			if (info.containsKey("shipNm")) {
				info.put("shipNm", ScpDbUtil.scpDec(String.valueOf(info.get("shipNm")), cryptoModeKey1));
			}
			if (info.containsKey("userId")) {
				info.put("userId", ScpDbUtil.scpDec(String.valueOf(info.get("userId")), cryptoModeKey1));
			}
			if (info.containsKey("userName")) {
				info.put("userName", ScpDbUtil.scpDec(String.valueOf(info.get("userName")), cryptoModeKey1));
			}
			if (info.containsKey("username")) {
				info.put("username", ScpDbUtil.scpDec(String.valueOf(info.get("username")), cryptoModeKey1));
			}
		}

	}

	private void toDecList(Map<String, Object> m) {
		Map<String, Object> item = (Map<String, Object>) m;

		if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화
			if (item.containsKey("assetName")) {
				item.put("assetName", ScpDbUtil.scpDec(String.valueOf(item.get("assetName")), cryptoModeKey1));

			}
			if (item.containsKey("affiliation")) {
				item.put("affiliation", ScpDbUtil.scpDec(String.valueOf(item.get("affiliation")), cryptoModeKey1));

			}
			if (item.containsKey("shipNm")) {
				item.put("shipNm", ScpDbUtil.scpDec(String.valueOf(item.get("shipNm")), cryptoModeKey1));
			}
			if (item.containsKey("userId")) {
				item.put("userId", ScpDbUtil.scpDec(String.valueOf(item.get("userId")), cryptoModeKey1));
			}
			if (item.containsKey("userName")) {
				item.put("userName", ScpDbUtil.scpDec(String.valueOf(item.get("userName")), cryptoModeKey1));
			}

			if (item.containsKey("username")) {
				item.put("username", ScpDbUtil.scpDec(String.valueOf(item.get("username")), cryptoModeKey1));
			}
			if (item.containsKey("workContent")) {
				item.put("workContent", ScpDbUtil.scpDec(String.valueOf(item.get("workContent")), cryptoModeKey1));
			}
			
			if (item.containsKey("terminalIp")) {
				item.put("terminalIp", StringUtil
						.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("terminalIp")), cryptoModeKey1))));
			}
		}
		
		if (item.containsKey("srcIp") && item.get("srcIp") instanceof Boolean == false) {
			if (cryptoMode.equals("Y")) {// 암호화된 필드 복호화
				log.debug("before srcIp:{}", item.get("srcIp"));
				log.debug("after srcIp1:{}", ScpDbUtil.scpDec(String.valueOf(item.get("srcIp")), "KEY1"));
				item.put("srcIp", StringUtil
						.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("srcIp")), cryptoModeKey1))));
			} else {
				item.put("srcIp", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("srcIp")))));
			}
		}
		if (item.containsKey("dstIp") && item.get("dstIp") instanceof Boolean == false) {
			if (cryptoMode.equals("Y")) {
				item.put("dstIp", StringUtil
						.longToIp(Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("dstIp")), cryptoModeKey1))));

			} else {
				item.put("dstIp", StringUtil.longToIp(Long.parseLong(String.valueOf(item.get("dstIp")))));
			}
		}

		if (item.containsKey("ipAddress")) {
			if (cryptoMode.equals("Y")) {
				item.put("ipAddress", StringUtil.longToIp(
						Long.parseLong(ScpDbUtil.scpDec(String.valueOf(item.get("ipAddress")), cryptoModeKey1))));

			} 
		}

	}

	@AfterThrowing(pointcut = "commonPointcut()", throwing = "ex")
	public void errorInterceptor(JoinPoint joinPoint, Throwable ex) throws Exception {
		ex.printStackTrace();

	}

	@Around("@annotation(mil.ln.ncos.annotation.NoAuth) && @ annotation(target)")
	public Object calllNoAuth(ProceedingJoinPoint joinPoint, NoAuth target) throws Throwable {
		log.debug("ControllerAspect calllNoAuth:" + joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName());
		return joinPoint.proceed();

	}

}
