package org.dream.dreambox.common.component.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

public class LuceneIndexUpdateTask {

    private static final String INDEX_PATH = "f:/lucene/index_main";
    
    private static final String INDEX_PATH_BACKUP = "f:/lucene/index_backup";

    private static final String SOURCE_PATH = "f:/lucene/txt";
    
    /**
     * 应用系统全局检索实例。
     */
    private LuceneContext searcher;

    public void index(){
        // 1、取得当前索引库路径，设置备用索引库
        String currentPath = null;
        String backupPath = null;
        if(INDEX_PATH.equals(searcher.getIndexPath())){
            backupPath = INDEX_PATH_BACKUP;
            currentPath = INDEX_PATH;
        }else{
            backupPath = INDEX_PATH;
            currentPath = INDEX_PATH_BACKUP;
        }
        // 创建“写”LuceneSearcher，做索引更新。
        LuceneContext searcher4Write = new LuceneContext(currentPath);
        // 2、取得新数据。
        File fileDir = new File(SOURCE_PATH);
        File[] files = fileDir.listFiles();
        // 3、更新索引库索引。
        for (int i = 0; i < files.length; i++) {
            Document doc = file2Document(files[i]);
            searcher4Write.createIndex(doc);
        }
        // 4、标记新数据为已更新。
        //TODO
        
        // 5、切换索引库。
        searcher.setIndexPath(backupPath);
    }
    
    private Document file2Document(File file) {
        Document doc = new Document();
        doc.add(new Field("title", file.getName(), Store.YES, Index.ANALYZED));
        doc.add(new Field("content", readFileContent(file), Store.YES, Index.ANALYZED));
        return doc;
    }

    private String readFileContent(File file) {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
            }
            System.out.println(content.toString());
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
