package com.example.springbatchexample.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.springbatchexample.model.User;

@Component
public class Processor implements ItemProcessor<User,User>{
	
	private static final Map<String,String> DEPT_NAMES=new HashMap<>();
	
	public Processor() {
		DEPT_NAMES.put("001", "Technology");
		DEPT_NAMES.put("002", "Analytics");
		DEPT_NAMES.put("003", "Maintanace");
		
		
	}
	

	@Override
	public User process(User user) throws Exception {
		String deptNo=user.getDept();
		String deptName=DEPT_NAMES.get(deptNo);
		user.setDept(deptName);
		return user;
	}

}
