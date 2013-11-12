<%@page import="java.io.*"%>
<%@page import="org.json.*"%>

<%

/*
String a = request.getParameter("a");
String t = request.getParameter("t");

//File creation
String strPath = "/home/kfquinn/example.txt";
File strFile = new File(strPath);
//File appending
BufferedWriter bw = new BufferedWriter(new FileWriter(strFile, true));
bw.write(t+": "+a+"\n");
bw.flush();
bw.close();
out.print("done!");
*/


JSONArray json = new JSONArray(request.getParameter("json"));
String strPath = "/home/kfquinn/example.txt";
File strFile = new File(strPath);
BufferedWriter bw = new BufferedWriter(new FileWriter(strFile, true));
bw.write(json+"\n");
bw.flush();
bw.close();

//HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//String ipAddress = request.getRemoteAddr();
//out.print(ipAddress);

%> 