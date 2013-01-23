package edu.utep.cybershare.DerivA.util;

public class ServerCredentials {

	private String username, password, serverURL, project;
	
	public void setUsername(String str){username = str;}
	public String getUsername(){return username;}
	
	public void setPassword(String str){password = str;}
	public String getPassword(){return password;}
	
	public void setServerURL(String str){serverURL = str;}
	public String getServerURL(){return serverURL;}
	
	public void setProject(String str){project = str;}
	public String getProject(){return project;}
	
	public void printCredentials(){
		System.out.println("---------------------------------");
		System.out.println("Server: " + serverURL);
		System.out.println("Project: " + project);
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		System.out.println("---------------------------------");
	}
	
}
