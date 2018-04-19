package main;

import java.io.File;

import org.apache.maven.shared.invoker.MavenInvocationException;

import saferefactor.core.Parameters;
import saferefactor.core.SafeRefactor;
import saferefactor.core.SafeRefactorImp;
import saferefactor.core.util.Project;
import utils.MavenHandler;

public class Test2 {
	private File sourceProjectFolder;
	private File targetProjectFolder;
	private double timeLimit;
 

	public Test2(File sourceProjectFolder, File targetProjectFolder) throws Exception {
		this.sourceProjectFolder = sourceProjectFolder;
		this.targetProjectFolder = targetProjectFolder;
	}
	
	public File getSourceProjectFolder() {
		return sourceProjectFolder;
	}

	public void setSourceProjectFolder(File sourceProjectFolder) {
		this.sourceProjectFolder = sourceProjectFolder;
	}

	public File getTargetProjectFolder() {
		return targetProjectFolder;
	}

	public void setTargetProjectFolder(File targetProjectFolder) {
		this.targetProjectFolder = targetProjectFolder;
	}
	
	public double getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(double timeLimit) {
		this.timeLimit = timeLimit;
	}

	public boolean hasSameBehavior() throws Exception {
		Project source = createProject(sourceProjectFolder);
		Project target = createProject(targetProjectFolder);
		execute(source,target);
		return true;
	}
	
	
	
	private Project createProject(File projectFolder) throws MavenInvocationException {
		Project project = new Project();
		project.setProjectFolder(sourceProjectFolder);
		project.setBuildFolder(new File(projectFolder, "target" + File.separator + "classes"));
		project.setSrcFolder(new File(projectFolder, "src" + File.separator + "main" + File.separator + "java"));
		File libFolder = new MavenHandler().copyDependencies(projectFolder);
		if (libFolder != null)
			project.setLibFolder(libFolder);
		return project;
	}
	
	private boolean execute(Project source, Project target) throws Exception {
		Parameters parameters = new Parameters();
		parameters.setTimeLimit(timeLimit);
		
		boolean sourceBin = true;
		if(!source.getBuildFolder().exists()) {
			source.getBuildFolder().mkdirs();
			sourceBin = false;
		}
		boolean targetBin = true;
		if(!target.getBuildFolder().exists()) {
			source.getBuildFolder().mkdirs();
			targetBin = false;
		}
		parameters.setCompileProjects(!(sourceBin && targetBin));
		
		SafeRefactor sr = new SafeRefactorImp(source, target,parameters);
		sr.checkTransformation();
		return sr.getReport().isRefactoring();
	}
	
	
	

}
