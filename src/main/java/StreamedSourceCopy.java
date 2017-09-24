import net.htmlparser.jericho.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class StreamedSourceCopy {
    public static void main(String[] args) throws Exception {
        String sourceUrlString="/home/myuser/Downloads/tuik_data/disticaret2009-13_files.html";
        if (args.length==0)
            System.err.println("Using default argument of \""+sourceUrlString+'"');
        else
            sourceUrlString=args[0];
        if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
        StreamedSource streamedSource=new StreamedSource(new URL(sourceUrlString));
        //streamedSource.setBuffer(new char[65000]); // uncomment this to use a fixed buffer size
        Writer writer=null;
        try {
            writer=new OutputStreamWriter(new FileOutputStream("StreamedSourceCopyOutput.html"),streamedSource.getEncoding());
            System.out.println("Processing segments:");
            int lastSegmentEnd=0;
            StringBuilder innerContent=new StringBuilder();
            boolean insideParagraphElement=false;
            for (Segment segment : streamedSource) {
               // System.out.println(segment.getDebugInfo());
                if (segment.getEnd()<=lastSegmentEnd) continue; // if this tag is inside the previous tag (e.g. a server tag) then ignore it as it was already output along with the previous tag.
                lastSegmentEnd=segment.getEnd();
                if (segment instanceof Tag) {
                    Tag tag=(Tag)segment;
                    String name = tag.getName();
                    if(name.equals(HTMLElementName.TABLE)) {
                        System.out.println("table");
                    }else if(name.equals(HTMLElementName.TR)){
                        if (tag instanceof StartTag) {
                            System.out.print("\nTR: |");
                        }
                    }else if(name.equals(HTMLElementName.TD)){
                        if (tag instanceof StartTag) {
                            insideParagraphElement=true;
                            innerContent.setLength(0);
                            String colspan = ((StartTag) tag).getAttributeValue("colspan");
                            colspan = (colspan == null)? "1":colspan;
                            for(int i=0;i<Integer.parseInt(colspan);i++)
                                System.out.print(" | " );
                        } else { // tag instanceof EndTag
                            insideParagraphElement=false;
                            System.out.print(innerContent.toString());
                        }
                    }

                } else if (segment instanceof CharacterReference) {
                    CharacterReference characterReference=(CharacterReference)segment;
                    if (insideParagraphElement){
                        characterReference.appendCharTo(innerContent);
                    }

                } else {// HANDLE PLAIN TEXT
                    if (insideParagraphElement){
                        innerContent.append(segment);
                    }

                }
                // unless specific handling has prevented getting to here, simply output the segment as is:
                //writer.write(segment.toString());
            }
            writer.close();
            System.err.println("\nA copy of the source document has been output to StreamedSourceCopyOuput.html");
        } catch (Exception ex) {
            if (writer!=null) try {writer.close();} catch (IOException ex2) {}
            throw ex;
        }
    }

    private static void displayColumns(List<Element> allElements) {
        for (Element element : allElements) {
            System.out.print("| " + element.getContent());
        }

    }
}
