package org.dream.dreambox.common.component.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

public class LuceneIndexGenerator {
    private static final String SOURCE_PATH = "f:/lucene/txt";
    private static final String INDEX_PATH  = "f:/lucene/index";

    /**
     * 创建修造方案索引。
     */
    public static void generateIndex() {
        System.out.println("索引程序启动...");
        try {
            deleteIndexIfExist(INDEX_PATH);
            createIndex();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("索引创建失败！");
        }
    }

    /**
     * 创建索引时，检测索引库，删除旧索引。
     * 
     * @param path
     */
    private static void deleteIndexIfExist(String path) {
        System.out.println("正在检测索引库: " + path);
        File indexDir = new File(path);
        File[] indexFiles = indexDir.listFiles();
        if (indexFiles.length != 0) {
            for (int i = 0; i < indexFiles.length; i++) {
                indexFiles[i].delete();
            }
            System.out.println("过时索引已删除.");
        }
    }

    /**
     * 创建索引。
     * 
     * @param path
     * @throws ServiceException
     * @throws DaoException
     */
    private static void createIndex() throws Exception {
        System.out.println("开始建立索引，请等待...");
        File fileDir = new File(SOURCE_PATH);
        File[] files = fileDir.listFiles();
        LuceneContext context = new LuceneContext();
        for (int i = 0; i < files.length; i++) {
            Document doc = file2Document(files[i]);
            context.createIndex(doc);
        }
        System.out.println("索引建立完成.");
    }

    /**
     * 将一个<code>SchemeVO</code>对象转换为<code>Document</code>对象，以便Lucene进行索引。
     * 
     * @param scheme
     * @param i
     * @return
     */
    private static Document file2Document(File file) {
        Document doc = new Document();
        doc.add(new Field("title", file.getName(), Store.YES, Index.ANALYZED));
        doc.add(new Field("content", readFileContent(file), Store.YES, Index.ANALYZED));
        return doc;
    }

    private static String readFileContent(File file) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        generateIndex();
    }
}
