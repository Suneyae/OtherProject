package com.wyl.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.wyl.entity.Student;
import com.wyl.entity.Student2;
import com.wyl.util.UtilDB;

public class DAOStudent {
	private static SqlSession sqlsession = null;
	{
		try {
			sqlsession = UtilDB.getSqlSession();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DAOStudent() {
	}
	/**
	 * 删除学生表
	 * @return
	 */
	public int delStu(){
		
		int num = 0;
		num = sqlsession.delete("Student.mydelstu");
		sqlsession.commit();
		System.out.println("成功删除"+num+"条数据");
		return num;
	}
	
	/**
	 * 批量删除数据
	 * @throws IOException 
	 */
	@Test
	public static void batchDel() throws IOException{
		Map<String,Object> map = new HashMap<>();
		Student2 stu2 = new Student2();
		List wdId = new ArrayList<>();
		for(int i=0;i<30;i++){
			wdId.add(i, i);
		}
		System.out.println(wdId);
		stu2.setWdId(wdId);
		sqlsession = UtilDB.getSqlSession();
		sqlsession.delete("batchDelStu", stu2);
	}
	
	public static void queryStu() throws IOException{
		Student stu2 = new Student(12,"","");
		sqlsession = UtilDB.getSqlSession();
		List<Student> list = sqlsession.selectList("Student.queryStudentByMap", stu2);
		System.out.println("queryStu:"+list.get(0).toString());
	}
	
	public static void main(String[] args) throws IOException {
		/*SqlSession sqlsession = UtilDB.getSqlSession();
		List<Student> list = sqlsession.selectList("Student.queryStudent");
		Student stu = null;
		for(int i=0;i<list.size();i++){
			stu = list.get(i);
			System.out.println("list:"+stu.toString());
		}*/
		
		/*DAOStudent dao = new DAOStudent();
		dao.delStu();*/
		
//		queryStu();
		batchDel();
	}
}
