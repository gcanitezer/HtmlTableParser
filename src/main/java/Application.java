
import java.io.*;
import java.net.URL;
import org.jsoup.*;

public class Application {
    public static void main(String[] args) throws IOException {
        File input = new File("/home/myuser/Downloads/tuik_data/disticaret2009-13_files.html");
        File output = new File("/home/myuser/Downloads/tuik_data/dt2009-13.csv");
        PrintStream outputStream = new PrintStream(output);

        org.jsoup.nodes.Document doc = Jsoup.parse(input,"windows-1254", "http://example.com");
        org.jsoup.select.Elements rows = doc.select("tr");
        String oldColumns[]=new String[30];
        int index = 0;
        for(org.jsoup.nodes.Element row :rows)
        {
            org.jsoup.select.Elements columns = row.select("td");
            for (org.jsoup.nodes.Element column:columns)
            {
                Integer i = 0;
                try {
                    i = Integer.parseInt(column.attr("colspan"));
                }catch (NumberFormatException e){

                }
                for (int j = 1; j < i; j++) {
                    if(oldColumns[index]!=null && !oldColumns[index].isEmpty()){
                        outputStream.print(oldColumns[index]);
                    }
                    outputStream.print(" |");
                    index ++;
                }
                String newCol = column.text();
                if(newCol.isEmpty()){
                    outputStream.print(oldColumns[index] + "|");
                }else {
                    outputStream.print(newCol + "|");
                    oldColumns[index] = newCol;

                }
                index++;
            }
            index = 0;
            outputStream.println();
        }




    }
}
