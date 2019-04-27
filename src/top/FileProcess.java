package top;

/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class FileProcess {

    private File file;
    private List<String> lsLines;

    public FileProcess() throws IOException {
        File currentDir = new File(System.getProperty("user.dir"));
        this.file = currentDir.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".txt"))[0];
        this.lsLines = FileUtils.readLines((File) this.file, (String) "UTF-8");
        this.lsLines.removeIf(line -> !line.contains("|"));
    }

    public FileProcess(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<String> getLsLines() {
        return this.lsLines;
    }

    public void setLsLines(List<String> lsLines) {
        this.lsLines = lsLines;
    }

    public List<String> search(String query) {
        ArrayList<String> lsResult = new ArrayList<>();
        query = query.replaceAll("[^a-zA-Z0-9|]", " ").replaceAll("  ", " ").toLowerCase().trim();
        for (String line : this.lsLines) {
            String fixedLine = line.replaceAll("[^a-zA-Z0-9|]", " ").replaceAll("  ", " ").toLowerCase().trim();
            if (!fixedLine.contains(query)) {
                continue;
            }
            lsResult.add(line);
        }
        return lsResult;
    }

}
