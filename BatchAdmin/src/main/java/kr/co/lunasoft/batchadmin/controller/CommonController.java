package kr.co.lunasoft.batchadmin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import kr.co.lunasoft.batchadmin.service.CommonService;
import lombok.extern.slf4j.Slf4j;

@Api(value = "LunaAdmin API", description = "[공통 코드 조회 API]")
@CrossOrigin(origins="*")
@RestController
@Slf4j
public class CommonController {

	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value="/common/codelist", method=RequestMethod.GET)
	@ResponseBody
	public String codelist(@RequestParam(name="menu", required=true) String menu) throws Exception{
		String result = commonService.getCommonCodeList(menu);
		log.info("codelist==>>{}",result);
		return result;
	}
	
	@RequestMapping(value="/common/corpsearch", method=RequestMethod.GET)
	@ResponseBody
	public String corpsearch(@RequestParam HashMap<String, Object> params) throws Exception{
		String result = commonService.getCorpList(params);
		log.info("corpsearch==>>{}",result);
		return result;
	}
	
	@RequestMapping(value="/common/code/{lcode}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> codedata(@PathVariable(required = true) String lcode) throws Exception{
		Map<String, Object> result = commonService.getCommonCodeData(lcode);
		log.info("codedata==>>{}",result);
		return result;
	}
}
