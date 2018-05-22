package main;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import utils.FileUtils;
import utils.GithubDownloader;
import utils.MavenHandler;
import utils.ZipExtractor;
import utils.XMLUtils;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.xml.sax.SAXException;



public class Purity {
	
	private String urlRepository;
	
	public Purity(String urlRepository){
		this.urlRepository=urlRepository;
	}
	
	public void setUrlRepository(String urlRepository){
		this.urlRepository=urlRepository;
	}
	
	public String getUrlRepository(){
		return this.urlRepository;	
	}
	

	public boolean check(String commit, String parent, double timeLimit) throws Exception {
		
		GithubDownloader git=new GithubDownloader(urlRepository);
		git.setLocation(git.getLocation()+File.separator+commit);
		
		File sourceFolder=new File(git.getLocation(),parent);
		File targetFolder=new File(git.getLocation(),commit);
		
		try {
			if(!sourceFolder.exists()) {
				File sourceFile=git.downloadCommit(parent);
				sourceFolder=ZipExtractor.extract(sourceFile, new File(git.getLocation(),parent));
				
				File targetFile=git.downloadCommit(commit);
				targetFolder=ZipExtractor.extract(targetFile, new File(git.getLocation(),commit));
				
				System.out.println(sourceFolder.getAbsolutePath());
				
				MavenHandler mavenHandler = new MavenHandler();
				mavenHandler.compileProject(sourceFolder);
				mavenHandler.compileProject(targetFolder);
			}
		
		
			Test test=new Test(sourceFolder,targetFolder, timeLimit);
			boolean hasSameBehaviour=test.hasSameBehaviour();
			this.deleteDirectory(git.getLocation());
			return hasSameBehaviour;
		} catch (Exception e) {
			this.deleteDirectory(git.getLocation());
			throw e;	
		}
	}
	
	
	private void deleteDirectory(File dir){
		File[] contents=dir.listFiles();
		for(File f: contents){
			if(f.isDirectory())
				deleteDirectory(f);
			else
				f.delete();
		}
		dir.delete();
	}
	
}
