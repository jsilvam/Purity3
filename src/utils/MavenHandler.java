package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MavenHandler {

	private Invoker invoker;
	
	public MavenHandler(String mavenHome) {
		invoker = new DefaultInvoker();
		invoker.setMavenHome(new File(mavenHome));
	}
	
	public MavenHandler() {
		invoker = new DefaultInvoker();
		if(System.getProperty("os.name").contains("Linux"))
			setMavenHome("/usr/share/maven");
		else
			setMavenHome("C:\\Program Files\\apache-maven-3.5.0");
	}
	
	public String getMavenHome() {
		return invoker.getMavenHome().getAbsolutePath();
	}

	public void setMavenHome(String mavenHome) {
		invoker.setMavenHome(new File(mavenHome));
	}

	public File copyDependencies(File projectFolder) throws MavenInvocationException {
		execute(new File( projectFolder,"pom.xml" ),
				Arrays.asList( "dependency:copy-dependencies"));
		File dependenciesFolder = new File(projectFolder, "target" + File.separator + "dependency");
		if(dependenciesFolder.exists())
			return dependenciesFolder;
		else
			return null;
	}
	
	public void compileProject(File projectFolder) throws Exception{
		execute(new File( projectFolder,"pom.xml" ), 
				Arrays.asList( "compile"));
		
//		List<File> modules=XMLUtils.getModules(new File(projectFolder,"pom.xml"));
//		XMLUtils.addPlugins(modules);
//		
//		execute(new File( projectFolder,"pom.xml" ), 
//				Arrays.asList( "install" , "-Dmaven.test.skip=true", "-Dmaven.javadoc.skip=true"));
	}
	
	public void execute(File pomFile, List<String> goals) throws MavenInvocationException {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( pomFile );
		request.setGoals( goals );
		invoker.execute( request );
	}
	
}
