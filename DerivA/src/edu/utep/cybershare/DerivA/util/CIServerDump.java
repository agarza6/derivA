package edu.utep.cybershare.DerivA.util;

import edu.utep.cybershare.ciclient.CIGet;
import edu.utep.cybershare.ciclient.CIPut;
import edu.utep.cybershare.ciclient.CIReturnObject;
import edu.utep.cybershare.ciclient.ciconnect.CIClient;
import edu.utep.cybershare.ciclient.ciconnect.CIKnownServerTable;

public class CIServerDump {

	CIClient ciclient;

	public CIServerDump(String ciServerPath, String username, String pass)
	{
		try
		{
			int rioServerId = CIKnownServerTable.getInstance().ciGetServerEntryFromURL(ciServerPath);
			CIKnownServerTable.getInstance().ciSetServerPassword(rioServerId,pass);
			CIKnownServerTable.getInstance().ciSetServerUsername(rioServerId,username);
			ciclient = new CIClient(rioServerId);

		}catch(Exception e)
		{e.printStackTrace();}
	}

	public String saveDataToCIServer(String fileName, String projectName, byte[] contents, boolean Binary){
		
		CIReturnObject ro = null;
		System.out.println(fileName);
		try
		{		
			ro = CIPut.ciUploadFile(ciclient, projectName, fileName, contents, CIClient.UDATA_TYPE, true, Binary);

			if(!ro.gStatus.equals("0")){
				System.out.println("Error: "+ro.gMessage);
			}
			else
			{
				System.out.println("Successful upload");
				System.out.println(ro.gFileURL);
			}  				
		}
		catch(Exception e){e.printStackTrace();}
		System.out.println("---> " + CIGet.ciGetNewResourcePath(ciclient, fileName, CIClient.UDATA_TYPE));
		return ro.gFileURL;
	}

	public String savePMLPToCIServer(String fileName, String projectName, byte[] contents, boolean Binary){
		//need to double check that server id is valid and available.
		CIReturnObject ro = null;
		System.out.println(fileName);
		try
		{		
			ro = CIPut.ciUploadFile(ciclient, projectName, fileName, contents, CIClient.PMLP_TYPE, true, Binary);

			if(!ro.gStatus.equals("0")){
				System.out.println("Error: "+ro.gMessage);
			}
			else
			{
				System.out.println("Successful upload");
				System.out.println(ro.gFileURL);
			}  				
		}
		catch(Exception e){e.printStackTrace();}
		return ro.gFileURL;
	}

	public String getUserInformation(){
		CIReturnObject ro = CIGet.ciGetUserInfo(ciclient);

		return ro.gUserPMLPURL;
	}

	public String getUserEmail(){
		CIReturnObject ro = CIGet.ciGetUserInfo(ciclient);
		String email = ro.gUserMail;
		if(email != null)
			return email;
		else
			return "";
	}

}