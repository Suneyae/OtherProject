package com.wyl.test;

import com.wyl.dao.DAOStudent;

public class TestDAO {

	public TestDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		DAOStudent dao = new DAOStudent();
		int num= dao.delStu();
		if(num>0){
			System.out.println("成功清空学生表");
		}
	}
}
