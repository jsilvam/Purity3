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
	
	private static void check(String repositoryUrl) throws IOException  {
		
		String dir;
		if(System.getProperty("os.name").contains("Linux"))
			dir  = "/home/jaziel/Dropbox/UFCG/Projeto/Dados/Projetos a serem testados"; //Linux
		else
			dir  = "C:\\Users\\Jaziel Moreira\\Dropbox\\UFCG\\Projeto\\Projetos a serem testados"; //Windows
		
		String aux=repositoryUrl.substring(repositoryUrl.lastIndexOf("/")+1);
		
		Purity p=new Purity(repositoryUrl);
		Scanner in = new Scanner(new FileReader(dir+"/Part 1/"+aux+".csv")).useDelimiter(";");
		FileWriter fw= new FileWriter(new File(dir+"/Part 2/"+aux+".csv"));
		
		
		
		PrintStream ps = new PrintStream(
			     new FileOutputStream(dir+"/Part 2/"+aux+" - log.txt", true));
		
		fw.write("Commit;isRefactoring\n");
		fw.flush();
		String commit="";
		
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
					System.out.println("Same Behaviour: "+sameBehaviour);
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
//		System.out.println(saferefactor.core.util.Constants.SEPARATOR);
		check("https://github.com/apache/incubator-dubbo");
//		check("https://github.com/square/okhttp");
//		check("https://github.com/google/guava");
//		check("https://github.com/zxing/zxing");
	}
}
