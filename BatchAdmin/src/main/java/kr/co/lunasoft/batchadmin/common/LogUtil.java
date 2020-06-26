package kr.co.lunasoft.batchadmin.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class LogUtil {
	private StringBuffer sB = new StringBuffer();
	private long startTime;
	private long finishTime;
	

	
	public void startJob() {
		startTime = System.nanoTime();
	}
	
	public void finishJob() {
		finishTime = System.nanoTime();
	}
	
	private void setSbLength() {
		sB.setLength(0);
	}
	
	public String executeTimeLog(long startTime, long finishTime){ 
		setSbLength();
		sB.append("execute ").append((finishTime - startTime) / 1000000).append(" ms");
		return sB.toString();
	}
	
	public String executeTimeLog(){
		setSbLength();
		sB.append("execute ").append((this.finishTime - this.startTime) / 1000000).append(" ms");
		return sB.toString();
	}
	
	public long executeTime() {
		return (this.finishTime - this.startTime) / 1000000;
	}
	
	public String catchLog(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}
	
	public String toString(HashMap<String, ?> hm) {
		if(hm == null) return null;
		
		setSbLength();
		List<String> keySet = new ArrayList<String>(hm.keySet());
		sB.append("\n##############################");
		for(String key : keySet) {
			sB.append("\n" + key + " : " + hm.get(key));
		}
		sB.append("\n##############################");
		return sB.toString();
	}
	
	public String toString(Object obj) {
		setSbLength();
		toString(sB, obj, 0);
		
		return sB.toString();
	}
	
	private void toString(StringBuffer sb, Object obj, int tabcnt) {
		String tab = "";
		for(int i = 0; i < tabcnt; i++) {
			tab += "\t";
		}
		
		try {
			if(obj != null) {
				sb.append("\n" + tab + "<" + obj.getClass().getName() + ">");
				
				Field[] fields = obj.getClass().getDeclaredFields();
				for(Field field : fields) {
					field.setAccessible(true);
					Type fieldType = field.getType();
					
					if(fieldType == java.util.List.class) { /** java.util.list 하위 객체 로깅 **/
						Object subObj = field.get(obj);
						
						if((ArrayList<?>)subObj != null) {
							for(Object subArray : (ArrayList<?>)subObj) {
								toString(sb, subArray, tabcnt + 1);
							}
						}
						
					} else if(fieldType.toString().indexOf("[L") > -1) { /** 원시형이 아닌 배열객체 로깅 **/
						Object[] subObj = (Object[]) field.get(obj);
						
						for(Object subArray : subObj) {
							if(subArray != null) {
								toString(sb, subArray, tabcnt + 1);
							}
						}
						
					} else if(!field.getType().isPrimitive() && field.getType() != String.class) { /** 원시형이 아닌 클래스 로깅 **/
						Object subObj = (Object) field.get(obj);
						toString(sb, subObj, tabcnt + 1);
						
					} else { /** 원시타입, String 객체 로깅 **/
						sb.append("\n\t" + tab + "<" + field.getName() + "><![CDATA[" + field.get(obj) + "]]></" + field.getName() + ">");
					}
				}
				
				sb.append("\n" + tab + "</" + obj.getClass().getName() + ">");
			}
			
		} catch (Exception e) {
			System.out.println(catchLog(e));
		}
	}
}