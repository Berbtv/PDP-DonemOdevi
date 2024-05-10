/**
*
* @author Berat Yılmaz berat.yilmaz4@ogr.sakarya.edu.tr
* @since 06.04.2024
* <p>
* Kullanıcıdan alınan linkteki repoyu klonlar ve dosyanın içindeki (sınıf içeren) .java uzantılı dosyaların analizini yapar.
* </p>
*/
package odev;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class deneme {
	
	
	 public static void main(String[] args) 
	    {
	        try 
	        {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	            System.out.println("Lütfen GitHub deposunun URL'sini girin: ");
	            String repoUrl = reader.readLine();
	           
	            
	            // GitHub deposunu klonlama işlemi
	            ProcessBuilder builder = new ProcessBuilder();
	            builder.command("git", "clone", repoUrl);
	            builder.directory(new File(System.getProperty("user.dir")));
	            Process process = builder.start();
	            int exitCode = process.waitFor();
	            
	            
	            if (exitCode == 0) 
	            {
	            	
	                System.out.println("Depo başarıyla klonlandı.");

	                // *.java dosyalarını getirme
	                File directory = new File(System.getProperty("user.dir"));
	                  
	                List<File> javaFilesList = new ArrayList<>();
	                findJavaFiles(directory, javaFilesList);

	                

	                File[] javaFiles = javaFilesList.toArray(new File[0]);
	                
	                if (javaFiles.length > 0) 
	                {
	                	
	                    for (File javaFile : javaFiles) 
	                    {
	                    	
	                        // Dosya adından sınıf adını al
	                        String className = javaFile.getName().replace(".java", "");
	                        
	                        if(!className.equals("deneme")) {
	                        	if (containsClass(javaFile, className)) 
	                            {   
	                                analyzeFile(javaFile, className);
	                                
	                            }
	                        	
	                            else 
	                            {
	                               
	                            }
	                        	
	                        }
	                        /*String foldername= javaFiles.toString();
	                        File clonedDirectory = new File(System.getProperty("user.dir"), foldername); //Klonlanan dizini belirt
	                        if (clonedDirectory.exists()) {
	                            deleteDirectory(clonedDirectory);
	                            System.out.println("Klonlanan dosya başarıyla silindi.");
	                        } else {
	                            System.out.println("Klonlanan dosya bulunamadı.");
	                        }*/
	                        
	                    }
	                } 
	                else 
	                {
	                    System.out.println("Hiç *.java dosyası bulunamadı.");
	                }
	                
	            } 
	            else 
	            {
	                System.out.println("Depo klonlanırken bir hata oluştu.");
	            }
	            
	            
	        }
	        catch (IOException | InterruptedException e) 
	        {
	        	System.out.println("Hata burada mı? main catch");
	            e.printStackTrace();
	        }
	    }
	 
	 public static void findJavaFiles(File directory, List<File> javaFilesList) {
	        File[] files = directory.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isDirectory()) {
	                    // Alt dizin varsa rekürsif olarak bu metodu çağırarak içindeki dosyaları ara
	                	
	                    findJavaFiles(file, javaFilesList);
	                } else if (file.getName().endsWith(".java")) {
	                    // *.java dosyası bulunduğunda listeye ekle
	                	
	                    javaFilesList.add(file);
	                }
	            }
	        }
	    }
	
	public static boolean containsClass(File file, String className) {
	    // Dosyanın içeriğinde sınıf tanımı olup olmadığını kontrol eden metot
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            // Satırda sınıf adının bulunup bulunmadığını kontrol et
	            if (line.contains("class " + className) && !line.contains("interface")) {
	            		            	
	            		return true;
	                
	            }
	        }
	    } catch (IOException e) {
	    	System.out.println(className + "containsClass hatası");
	        e.printStackTrace();
	    }
	    return false;
	}
    
    public static void analyzeFile(File file, String className) 
    {
        double javadocLineCount = 0;
        double commentLineCount = 0;
        double codeLineCount = 0;
        double functionCount = 0;
        double LOC=0;
        
        
        System.out.println("Sınıf: " + className +".java");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {	
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Satırın başındaki ve sonundaki boşlukları temizleyelim
            	
                line = line.trim();
                
                // Satır her zaman sadece yorumların olduğu satır da olmayabilir.
                // Bu nedenle her satırın yorum veya kod olup olmadığını kontrol edelim.

                // Javadoc satırı mı? (Örneğin: /** veya * ...)
                if (line.matches("^\\s*/\\*\\*\\s*$")) 
                {
                	
                    while ((line = reader.readLine()) != null) 
                    {
                        if (line.matches("^\\s*\\*(?![/]).*")) 
                        {
                        	LOC++;
                            javadocLineCount++;
                            
                        }
                        
                        else 
                        {
                        	LOC++;
                            // Javadoc yorumu bittiği için döngüden çık
                            break;
                        }
                    }
                }
                // /* ve sonraki satırlarda * gelme durumu
                if (line.matches("^\\s*/\\*.*")) 
                {
                	
                    while ((line = reader.readLine()) != null) 
                    {
                        if (line.matches("^\\s*\\*(?![/]).*")) 
                        {
                        	LOC++;
                        	commentLineCount++;
                        	
                        }
                        
                        else 
                        {
                        	LOC++;
                            // Javadoc yorumu bittiği için döngüden çık
                            break;
                        }
                    }
                }
                
                // Yorum satırı mı? (Örneğin: // veya  ...)
                else if (line.matches("^.*//.*")) 
                {
                	if((line.matches("^\\S.*//.*")))
                	{
                		codeLineCount++;
                		
                	}
                    commentLineCount++;
                    
                }
                // Kod satırı mı?
                else if ( (!line.isEmpty()) && (!(line.matches(".*\\*/\\s*$"))) ) {
                    codeLineCount++;
                    
                    
                    Pattern pattern = Pattern.compile("(?m)^\\s*(public|private|protected)\\s+(static\\s+)?(\\w+)\\s+(\\w+)\\s*\\((.*?)\\).*?$");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) 
                    {
                        functionCount++;
                        
                    }
                    
                    String regex = "(public|private|protected)?\s+" + className + "\s*\\((.*?)\\)\s*\\{?";

                    Pattern pattern1 = Pattern.compile(regex);
                    Matcher matcher1 = pattern1.matcher(line);
                    while (matcher1.find()) 
                    {
                        functionCount++;
                        
                    }
                }

                // Toplam satır sayısını artır
                LOC++;
            }
              
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        
        
        System.out.println("Javadoc Satır Sayısı: " + (int) javadocLineCount);
        System.out.println("Yorum Satır Sayısı: " + (int) commentLineCount);
        System.out.println("Kod Satır Sayısı: " + (int) codeLineCount);
        System.out.println("LOC:  " + (int) LOC);
        System.out.println("Fonksiyon Sayısı:  " + (int) functionCount);

        
     
        double yg =( (javadocLineCount + commentLineCount) * 0.8 )/ functionCount;
        if(functionCount!=0) {
        	double yh = (codeLineCount / functionCount) * 0.3;

            
            double yorumSapmaYuzdesi = ( ( (100 * yg) / yh ) - 100 );
            System.out.println("Yorum Sapma Yüzdesi: %" + String.format("%.2f", yorumSapmaYuzdesi));
        }
        System.out.println("---------------------------------");
    }
    
    
   
    
    
    

   
	
    
    /*private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        System.out.println("Dosya silinirken hata oluştu: " + file.getAbsolutePath());
                    }
                }
            }
        }
        boolean deleted = directory.delete();
        if (!deleted) {
            System.out.println("Dizin silinirken hata oluştu: " + directory.getAbsolutePath());
        }
    }*/
    
}







