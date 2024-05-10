package odev;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class deneme {
	
	public static boolean containsClass(File file, String className) {
	    // Dosyanın içeriğinde sınıf tanımı olup olmadığını kontrol eden metot
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            // Satırda sınıf adının bulunup bulunmadığını kontrol et
	            if (line.contains("class " + className) && !line.contains("interface")) {
	            	System.out.println(className + "dosyası sınıf içermektedir.");
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
        int javadocLineCount = 0;
        int commentLineCount = 0;
        int codeLineCount = 0;
        int functionCount = 0;
        
        
        System.out.println("Sınıf: " + className);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {	
            String line;
            while ((line = reader.readLine()) != null) 
            {
                // Javadoc yorumu mu?
                if (line.matches("/\\*\\*.*?\\*/")) 
                {
                    javadocLineCount++;
                    System.out.println("?javadoc");
                }
                // Diğer yorum mu?
                else if (line.matches("//.*")) 
                {
                    commentLineCount++;
                    System.out.println("?yorum");
                }
                // Kod satırı mı?
                else if (line.matches("(?!//|\\/\\*).+")) 
                {
                    codeLineCount++;
                    System.out.println("?kod satırı");

                    // Fonksiyon mu?
                    if (line.matches("(public|private|protected)\\s+(static|final)?\\s+[\\w\\d<>]+\\s+([\\w\\d]+)\\(.*?\\)")) 
                    {
                        functionCount++;
                        System.out.println("? fonksiyon");
                    }
                }
                
            }
              
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        // YG ve YH değerlerini hesaplayın
        double yg =( (javadocLineCount + commentLineCount) * 0.8 )/ functionCount;
        double yh = codeLineCount / functionCount * 0.3;

        // Yorum Sapma Yüzdesini hesaplayın
        double yorumSapmaYuzdesi = (100 * yg) / yh;
        System.out.println("Javadoc Satır Sayısı: " + javadocLineCount);
        System.out.println("Yorum Satır Sayısı: " + commentLineCount);
        System.out.println("Kod Satır Sayısı: " + codeLineCount);
        System.out.println("LOC:  " + (codeLineCount + commentLineCount + javadocLineCount) );
        System.out.println("Fonksiyon Sayısı:  " + functionCount);
        System.out.println("Yorum Sapma Yüzdesi: " + yorumSapmaYuzdesi);
        System.out.println("----------------------------------------");
    }
    
    
    public static void deleteClonedDirectory(String clonedFolderPath) {
        File clonedDirectory = new File(clonedFolderPath);
        if (clonedDirectory.exists()) {
            try {
                Files.walk(Paths.get(clonedDirectory.getAbsolutePath()))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
                System.out.println("Klonlanan klasör başarıyla silindi.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Klonlanan klasör bulunamadı.");
        }
    }
    
    

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
                
                System.out.println("?");
                
                
                File[] allFiles = directory.listFiles();
                System.out.println("??");
                
                List<File> javaFilesList = new ArrayList<>();
                for (File file : allFiles) {
                    if (file.isDirectory()) {
                        // İç dizinlerdeki dosyaları da al
                        File[] innerFiles = file.listFiles((dir, name) -> name.endsWith(".java"));
                        if (innerFiles != null) {
                        	
                            javaFilesList.addAll(Arrays.asList(innerFiles));
                            System.out.println("!");
                        }
                    } else if (file.getName().endsWith(".java")) {
                        // Dizin içinde doğrudan *.java dosyası varsa al
                        javaFilesList.add(file);
                        System.out.println("!!");
                    }
                }
                System.out.println("???");
                File[] javaFiles = javaFilesList.toArray(new File[0]);

                System.out.println("???");
                if (javaFiles != null) 
                {
                    for (File javaFile : javaFiles) 
                    {
                        // Dosya adından sınıf adını al
                        String className = javaFile.getName().replace(".java", "");
                        System.out.println("!!!");
                        if (containsClass(javaFile, className)) 
                        {   
                        	System.out.println("!!!!");
                            analyzeFile(javaFile, className);
                        } 
                        else 
                        {
                            System.out.println(className + " dosyası içinde sadece sınıf yok, atlanıyor.");
                        }
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

	
}




/* if (directory.exists()) 
{
    if (!directory.getAbsolutePath().equals(System.getProperty("user.dir"))) 
    {
    	deleteClonedDirectory(System.getProperty("user.dir") + File.separator + repoFolderName);
    } 
    else 
    {
        System.out.println("Klonlanan klasör silinemedi. Kaynak kod klasörü ile aynı konumda.");
    }
}
else 
{
    System.out.println("Klonlanan klasör bulunamadı.");
}*/