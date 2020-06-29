package kr.co.lunasoft.batchadmin.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import kr.co.lunasoft.batchadmin.mapper.dbadmin.read.LoginMapper;
import kr.co.lunasoft.batchadmin.mapper.dbadmin.write.LoginWriteMapper;


@Service
public class LoginServiceImpl implements LoginService{

	@Resource
	private LoginMapper loginMapper;
	
	@Resource
	private LoginWriteMapper loginWriteMapper;	
	
	private Gson gson = new Gson();

	@Override
	public String loginUserInfoCheck(Map<String, String> params) throws Exception {
		String rtnResult = "";
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			
			String org_pw = params.get("password");
			String sha256hex = Hashing.sha256()
					  .hashString(org_pw, StandardCharsets.UTF_8)
					  .toString();
			params.put("hashPw", sha256hex);
			
			Map<String, Object> user = loginMapper.loginUserInfo(params);
			
			if(null != user) {
				result.put("user", user);
				result.put("userMenuMain", loginMapper.loginUserMenuMain(user.get("luna_auth_no").toString()));
				result.put("userMenuSub", loginMapper.loginUserMenuSub(user.get("luna_auth_no").toString()));
				
				loginWriteMapper.updateLastLoinDate(user.get("luna_id").toString());
			}else {
				int user_cnt = 0;
				user_cnt = loginMapper.checkExistsId(params);
				if(user_cnt == 0) {
					result.put("loginFailMsg", "존재하지 않는 ID 입니다.");
				}else {
					result.put("loginFailMsg", "비밀번호가 일치하지 않습니다.");
				}
			}
			
			rtnResult = gson.toJson(result);
			return rtnResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnResult;
	}

	@Override
	public String modifyPassWord(Map<String, Object> params) throws Exception {
		String rtnResult = "0001";
		Map<String, Object> pw_data = new HashMap<String, Object>();
		int chk_pw;
		try {
			
			String org_pw = params.get("cur_password").toString();
			String sha256hex = Hashing.sha256()
					  .hashString(org_pw, StandardCharsets.UTF_8)
					  .toString();
			pw_data.put("hashOrgPw", sha256hex);
			pw_data.put("luna_no", params.get("login_luna_no").toString());
			
			chk_pw = loginMapper.checkOrgPw(pw_data);
			
			if(chk_pw > 0) {
				String new_pw = params.get("new_password").toString();
				String new_pw_sha256hex = Hashing.sha256()
						  .hashString(new_pw, StandardCharsets.UTF_8)
						  .toString();
				pw_data.put("new_password", new_pw_sha256hex);
				
				loginWriteMapper.updatePassword(pw_data);
				
				rtnResult = "0000";
			}else {
				rtnResult = "0002";
			}
			
			return rtnResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnResult;
	}
	
}
