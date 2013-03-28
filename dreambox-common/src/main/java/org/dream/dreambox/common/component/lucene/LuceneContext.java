package org.dream.dreambox.common.component.lucene;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneContext {
    
    private static final Analyzer DEFAULT_ANALYZER = new StandardAnalyzer(Version.LUCENE_36);
    
    private static final String DEFAULT_INDEX_PATH = "f:/lucene/index";
    
    /**
     * 分词器。
     */
    private Analyzer analyzer;
    
    /**
     * <code>IndexWriter</code>实例。
     */
    private static IndexWriter indexWriter;
    
    /**
     * 索引库路径。
     */
    private String indexPath;

    public LuceneContext(){
        this(DEFAULT_INDEX_PATH, DEFAULT_ANALYZER);
    }
    
    public LuceneContext(String indexPath){
        this(indexPath, DEFAULT_ANALYZER);
    }
    
    public LuceneContext(String indexPath, Analyzer analyzer){
        this.indexPath = indexPath;
        this.analyzer = analyzer;
    }
    
    /**
     * 取得<code>IndexWriter</code>实例。
     * @return
     */
    public IndexWriter getIndexWriter() {
        try {
            if(indexWriter == null){
                IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
                Directory dir = FSDirectory.open(new File(indexPath));
                indexWriter = new IndexWriter(dir, iwc);
            }
            return indexWriter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 为单文档创建索引。
     * @param doc
     */
    public void createIndex(Document doc) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.addDocument(doc);
            indexWriter.commit();
            //indexWriter.forceMerge(1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (indexWriter != null){
                    indexWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 为多文档批量创建索引。
     * @param docs
     */
    public void createIndex(List<Document> docs){
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            for(Document doc : docs){
                indexWriter.addDocument(doc);
            }
            indexWriter.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (indexWriter != null){
                    indexWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(Term term) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.deleteDocuments(term);
            indexWriter.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (indexWriter != null){
                    indexWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 根据<code>Document</code>的id域删除相应文档的索引。前提是创建索引时必须添加id域Field。
     * @param id
     */
    public void deleteDocInIndex(String id){
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            Term term = new Term("id", id);
            indexWriter.deleteDocuments(term);
            indexWriter.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (indexWriter != null){
                    indexWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 根据指定的关键词检索各文档指定的<code>Field</code>域。
     * @param queryString 关键词。
     * @param firstResult 开始序号，由0开始。
     * @param maxResults 检索数量。
     * @param fields <code>String</code>数组。须要检索的域名称。
     * @return
     */
    public QueryResult search(String queryString, int firstResult, int maxResults, String[] fields){
        try {
            /*Map<String, Float> boosts = new HashMap<String, Float>();
            boosts.put("title", 3.0f);
            boosts.put("content", 1.0f);*/
            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36, fields, analyzer);
            TokenStream tokenStream = analyzer
                .tokenStream("content", new StringReader(queryString));
            tokenStream.addAttribute(CharTermAttribute.class);
            StringBuffer sb = new StringBuffer();
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = tokenStream
                    .getAttribute(CharTermAttribute.class);
                sb.append(charTermAttribute.buffer()).append(" ");
            }
            Query query = queryParser.parse(sb.toString().trim());
            return search(query, firstResult, maxResults);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 根据指定的关键词检索。默认检索title和content域。
     * @param queryString 关键词。
     * @param firstResult 开始序号，由0开始。
     * @param maxResults 检索数量。
     * @return
     */
    public QueryResult search(String queryString, int firstResult, int maxResults) {
        String[] fields = { "title", "content" };
        return search(queryString, firstResult, maxResults, fields);
    }

    /**
     * 根据指定的<code>Query</code>对象检索。
     * @param queryString 关键词。
     * @param firstResult 开始序号，由0开始。
     * @param maxResults 检索数量。
     * @return
     */
    public QueryResult search(Query query, int firstResult, int maxResults) {
        IndexSearcher indexSearcher = null;
        try {
            // 1. 查询
            IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexPath)));
            indexSearcher = new IndexSearcher(reader);
            TopDocs topDocs = indexSearcher.search(query, 10000);
            int recordCount = topDocs.totalHits;

            // 2 准备高亮器
            Formatter formatter = new SimpleHTMLFormatter("<font color='#DD0000'>", "</font>");
            Scorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(formatter, scorer);
            Fragmenter nameFragmenter = new SimpleFragmenter(30);
            Fragmenter contentFragmenter = new SimpleFragmenter(100);

            // 3. 取得当前页面内容
            int end = Math.min(firstResult + maxResults, topDocs.totalHits);
            List<QueryResultItem> recordList = new ArrayList<QueryResultItem>();
            for (int i = firstResult; i < end; i++) {
                Document doc = indexSearcher.doc(topDocs.scoreDocs[i].doc);
                // 使用高亮器
                highlighter.setTextFragmenter(nameFragmenter);
                String nc = highlighter.getBestFragment(analyzer, "title", doc.get("title"));
                if (nc == null) {
                    String name = doc.get("title");
                    int endIndex = Math.min(30, name.length());
                    nc = name.substring(0, endIndex);
                }
                highlighter.setTextFragmenter(contentFragmenter);
                String hc = highlighter.getBestFragment(analyzer, "content", doc.get("content"));
                if (hc == null) {
                    String content = doc.get("content");
                    int endIndex = Math.min(100, content.length());
                    hc = content.substring(0, endIndex);
                }
                recordList.add(new QueryResultItem(doc.get("id"), nc, hc));
            }
            // 4. 返回结果
            return new QueryResult(recordCount, recordList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (indexSearcher != null){
                    indexSearcher.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateIndex(Term term, Document doc) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.updateDocument(term, doc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (indexWriter != null)
                    indexWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        LuceneContext index = new LuceneContext();
        //index.delete("0");
        QueryResult res = index.search("1 2 3", 0, 5);
        System.out.println(res.getRecordCount() + "条结果");
        for(QueryResultItem item : res.getRecordList()){
            System.out.println("-----------------------------------------------");
            System.out.println("id     : " + item.getId());
            System.out.println("title  : " + item.getTitle());
            System.out.println("content: " + item.getContent());
        }
    }
    
    public void setIndexPath(String indexPath){
        this.indexPath = indexPath;
    }
    
    public String getIndexPath(){
        return this.indexPath;
    }
}
