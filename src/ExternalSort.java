import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by AlexChen on 2017-02-12.
 */
public class ExternalSort {

    // size unit is KB;
    public static int TOUPLE_PER_BLOCK = 40;

    //public static int BLOCK_SIZE = 4;
    //public static int MEMORY_SIZE = 5000;

    public static int MEMORY_BLOCK_SIZE = 4;


    private static CommonObject readCommonObject(BufferedReader br) throws IOException{
        String line = br.readLine();
        if(line == null){
            return null;
        }else{
            CommonObject obj = new CommonObject(line);
            return obj;
        }
    }

    private static void writeCommonObject(CommonObject value, FileWriter fw) throws IOException{
        fw.write(value+"\n");
    }

    private static File mergeFile(File sorted_file_1, File sorted_file_2, File output) throws IOException{
        BufferedReader br1 = new BufferedReader(new FileReader(sorted_file_1));
        BufferedReader br2 = new BufferedReader(new FileReader(sorted_file_2));

        FileWriter fw = new FileWriter(output, true);

        CommonObject a = readCommonObject(br1);
        CommonObject b = readCommonObject(br2);

        boolean finished = false;

        while (!finished) {
            if (a != null && b != null) {
                if (a.compareTo(b) < 0) {
                    writeCommonObject(a, fw);
                    a = readCommonObject(br1);
                } else {
                    writeCommonObject(b, fw);
                    b = readCommonObject(br2);
                }
            } else if (a == null && b != null) {
                writeCommonObject(b, fw);
                b = readCommonObject(br2);
            } else if (b == null && a != null) {
                writeCommonObject(a, fw);
                a = readCommonObject(br1);
            } else {
                finished = true;
            }
        }
        br1.close();
        br2.close();
        fw.close();
        return output;
    }

    private static void chunkFileIntoRuns(File file, String dir) throws IOException{
        ArrayList<CommonObject> tep_list = new ArrayList<CommonObject>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        int file_number_id = 0;
        Boolean finished = false;
        while(!finished){
            // keep read tuple from base file. until end.
            for(int i=0; i<(TOUPLE_PER_BLOCK * MEMORY_BLOCK_SIZE); i++){
                CommonObject line = readCommonObject(br);
                if(line != null) {
                    tep_list.add(line);
                }else{
                    finished =  true;
                    br.close();
                    break;
                }
            }

            // in-build function to sort the list.
            Collections.sort(tep_list);

            File run = new File(dir+file_number_id+".txt");
            FileWriter fw = new FileWriter(run, true);

            for(CommonObject row: tep_list){
                writeCommonObject(row, fw);
            }

            tep_list.clear();
            fw.close();

            file_number_id++;
        }
    }

    // This function for get prefix of file name.
    private static String getFileName(File file){
        String fullName = file.getName();
        String prefix = fullName.split("\\.")[0];
        return prefix;
    }


    private static File externalSort(File file, String dir) throws IOException{
        chunkFileIntoRuns(file, dir);
        File path = new File(dir);
        File [] files = path.listFiles();
        while(files.length != 1){
            mergeFile(files[0], files[1], new File(dir+getFileName(files[0])+"_"+getFileName(files[1])+".txt"));
            files[0].delete();
            files[1].delete();
            files = path.listFiles();
        }
        File newFile = new File(dir+"sorted_"+getFileName(file)+".txt");
        files[0].renameTo(newFile);

        return newFile;
    }

    public static void main(String[] args) {
        try {
            File file_1 = new File("./Employees.txt");
            File file_3 = new File("./Project.txt");

            File sorted_file_1 = externalSort(file_1, "./temporary_dir_for_Emp/");
            File sorted_file_3 = externalSort(file_3, "./temporary_dir_for_Pro/");

            File final_result = new File("./final_result.txt");
            if(!final_result.exists()){
                final_result.createNewFile();
            }

            mergeFile(sorted_file_1, sorted_file_3, final_result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
