package com.tcs.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.util.DatabaseUtil;

/**
 * Servlet implementation class ProcessTrigger
 */
public class configBeacon extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request,response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request,response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String referer = request.getHeader("referer");
		if(referer==null){
			response.sendRedirect("unauthorisedAccess.jsp");
			return ;
		}
		
		HttpSession session = request.getSession();

		try{
			
			
			// parameters from session
			String val = session.getAttribute("userID").toString();
			
			int userID = Integer.valueOf(val);
			String uuid="";
			if(request.getParameter("uuid")==null||request.getParameter("uuid")==""){
			uuid=" ";
			}else{
				uuid=request.getParameter("uuid").toString();
			}
			Connection cnn = DatabaseUtil.getConnection();
			PreparedStatement st1 = cnn
					.prepareStatement("update geo_user set uuid=? where user_id=?");
			st1.setString(1, uuid);
			st1.setInt(2,userID);
			int count = st1.executeUpdate();
			if(count>0){
				System.out.println("updated");
				session.setAttribute("config", "success");
				request.getRequestDispatcher("configBeacon.jsp").forward(request,response);
				
			}else{
				session.setAttribute("config", "failed");
				request.getRequestDispatcher("configBeacon.jsp").forward(request,response);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			session.setAttribute("config", "failed");
			request.getRequestDispatcher("configBeacon.jsp").forward(request,response);
			
		}

	}
}
