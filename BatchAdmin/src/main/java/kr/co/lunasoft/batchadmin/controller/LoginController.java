package kr.co.lunasoft.batchadmin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kr.co.lunasoft.batchadmin.service.LoginService;
import lombok.extern.slf4j.Slf4j;

@Api(value = "LunaAdmin API", description = "[통합어드민 로그인 API]")
@CrossOrigin(origins="*")
@RestController
@Slf4j
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value="/login/loginsubmit", method=RequestMethod.POST)
	@ResponseBody
	public String loginsubmit(@RequestBody HashMap<String, Object> params) throws Exception{
		String result = loginService.loginUserInfoCheck((Map<String, String>) params.get("params"));
		log.info("loginsubmit==>>{}",result);
		return result;
	}
	
	@RequestMapping(value="/login/logout", method=RequestMethod.GET)
	@ResponseBody
	public String logout(@RequestParam HashMap<String, String> params) throws Exception{
		return "true";
	}
	
	@RequestMapping(value="/mypage/passwordmod", method=RequestMethod.POST)
	@ResponseBody
	public String passwordmod(@RequestBody HashMap<String, Object> params) throws Exception{
		@SuppressWarnings("unchecked")
		String result = loginService.modifyPassWord((Map<String, Object>) params.get("params"));
		log.info("passwordmod==>>{}",result);
		return result;
	}
}
