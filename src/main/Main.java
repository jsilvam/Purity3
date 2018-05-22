 package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
	
	private static double timeLimit = 120;
	private static String dataFolder = "Projetos Dataset";
	private static File dir = new File(System.getProperty("user.home") + File.separator + 
			"Dropbox" + File.separator + "UFCG" + File.separator + 
			"Projeto" + File.separator + "Dados" + File.separator + dataFolder);
	
	private static void check(String repositoryUrl) throws IOException  {
		
		String projectName=repositoryUrl.substring(repositoryUrl.lastIndexOf("/")+1);
		
		File refactoringsFile = new File(dir,"Part 1" + File.separator + projectName+".csv");
		File isRefactoringFile = new File(dir, "Part 2" + File.separator + projectName+".csv");
		File logFile = new File(dir, "Part 2" + File.separator + "log" + File.separator + projectName+" - log.txt");
		
		Purity p=new Purity(repositoryUrl);
		Scanner in = new Scanner(refactoringsFile).useDelimiter(";");
		FileWriter fw= new FileWriter(isRefactoringFile);
		
		PrintStream ps = new PrintStream(
			     new FileOutputStream(logFile, true));
		
		fw.write("Commit;isRefactoring\n");
		fw.flush();
		String commit="";
		String aux;
		
		in.nextLine();
		while(in.hasNext()) {
			in.next();
			aux=in.next();
			if(!aux.equals(commit)) {
				commit=aux;
				String parent=in.next();
				fw.write(commit+";");
				try {
					boolean sameBehaviour=p.check(commit, parent, timeLimit);
					if(sameBehaviour)
						fw.write(1+"\n");
					else
						fw.write(0+"\n");
					System.out.println("Same Behaviour [Commit]: "+sameBehaviour);
				} catch (Exception e) {
					fw.write((-1)+"\n");
					System.out.println("Same Behaviour: Error");
					ps.println("Commit error: "+commit);
					e.printStackTrace(ps);
					ps.flush();
					e.printStackTrace();
				}
				fw.flush();
				
			}
			in.nextLine();
		}
		in.close();
		ps.close();
		fw.close();
	}

	public static void main(String[] args) throws Exception {
		File urlsFile = new File ("");
		Scanner urls = new Scanner(urlsFile);
		
		while(urls.hasNextLine()){
			try {
				check(urls.nextLine());
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		urls.close();
	}
}
